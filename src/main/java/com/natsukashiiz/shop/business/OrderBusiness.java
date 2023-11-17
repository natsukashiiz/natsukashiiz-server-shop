package com.natsukashiiz.shop.business;

import co.omise.models.Charge;
import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.PayOrderResponse;
import com.natsukashiiz.shop.payment.PaymentService;
import com.natsukashiiz.shop.payment.model.ChargeData;
import com.natsukashiiz.shop.payment.model.EventData;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.service.PushNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class OrderBusiness {
    private final OrderService orderService;
    private final AuthService authService;
    private final PushNotificationService pushNotificationService;
    private final PaymentService paymentService;

    public List<OrderResponse> myOrders() throws BaseException {
        return orderService.myOrderListByLatest(authService.getCurrent());
    }

    public OrderResponse myOrderById(String orderId) throws BaseException {
        return orderService.myOrderById(UUID.fromString(orderId));
    }

    public List<OrderResponse> create(List<CreateOrderRequest> requests) throws BaseException {
        List<OrderResponse> responses = orderService.create(requests, authService.getCurrent());
        if (!responses.isEmpty()) {
            for (OrderResponse order : responses) {
                NotificationPayload notify = new NotificationPayload();
                notify.setTo(authService.getCurrent());
                notify.setMessage("you have new order no " + order.getOrderId());
                pushNotificationService.dispatchTo(notify);
            }
        }
        return responses;
    }

    public PayOrderResponse pay(PayOrderRequest request) throws BaseException {
        OrderResponse order = myOrderById(request.getOrderId());
        Charge charge = paymentService.charge(order.getTotalPrice(), request.getSource(), order.getOrderId().toString());
        log.debug("status: {}", charge.getStatus());
        log.debug("failed code: {}", charge.getFailureCode());
        log.debug("failed text: {}", charge.getFailureMessage());
        log.debug("link: {}", charge.getAuthorizeUri());
        orderService.updateChargeId(order.getOrderId(), charge.getId());
        return PayOrderResponse.builder()
                .orderId(order.getOrderId())
                .url(charge.getAuthorizeUri())
                .build();
    }

    public void updateOrderFromWebhook(EventData request) {
        if (request.getKey().equals("charge.complete")) {
            ChargeData data = request.getData();
            String orderId = data.getMetadata().getOrderId();
            String chargeId = data.getId();

            try {
                OrderResponse order = myOrderById(orderId);
                if (order.getStatus() != OrderStatus.PENDING) {
                    log.warn("UpdateOrderFromWebhook-[block]:(status not pending). orderId:{}", order.getOrderId());
                    throw OrderException.invalid();
                }

                if (!order.getOrderId().toString().equals(orderId)) {
                    log.warn("UpdateOrderFromWebhook-[block]:(not orderId). orderId:{}", order.getOrderId());
                    throw OrderException.invalid();
                }

                if (data.getStatus().equals("successful")) {
                    orderService.updateStatus(UUID.fromString(orderId), OrderStatus.SUCCESS);
                } else {
                    log.warn("UpdateOrderFromWebhook-[block]:(not successful). orderId:{}", order.getOrderId());
                    orderService.updateStatus(UUID.fromString(orderId), OrderStatus.FAIL);
                }
            } catch (BaseException e) {
                e.printStackTrace();
            }
        } else {
            log.debug("UpdateOrderFromWebhook-[next]. data:{}", request);
        }
    }
}
