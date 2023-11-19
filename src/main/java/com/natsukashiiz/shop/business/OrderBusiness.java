package com.natsukashiiz.shop.business;

import co.omise.models.Charge;
import co.omise.models.ChargeStatus;
import co.omise.models.Event;
import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.OrderItem;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.exception.PaymentException;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.PayOrderResponse;
import com.natsukashiiz.shop.payment.PaymentService;
import com.natsukashiiz.shop.repository.OrderRepository;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.service.PushNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class OrderBusiness {
    private final OrderService orderService;
    private final AuthService authService;
    private final PushNotificationService pushNotificationService;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    public List<OrderResponse> myOrders() throws BaseException {
        return orderService.myOrderListByLatest(authService.getCurrent())
                .stream()
                .map(OrderResponse::build)
                .collect(Collectors.toList());
    }

    public OrderResponse myOrderById(String orderId) throws BaseException {
        return OrderResponse.build(orderService.myOrderById(UUID.fromString(orderId)));
    }

    public OrderResponse create(List<CreateOrderRequest> requests) throws BaseException {
        Order order = orderService.create(requests, authService.getCurrent());
        NotificationPayload notify = new NotificationPayload();
        notify.setType(NotificationType.ORDER);
        notify.setTo(authService.getCurrent());
        notify.setMessage("you have new order no " + order.getId());
        pushNotificationService.dispatchTo(notify);
        return OrderResponse.build(order);
    }

    public PayOrderResponse pay(PayOrderRequest request) throws BaseException {

        if (ObjectUtils.isEmpty(request.getOrderId())) {
            log.warn("Pay-[block]:(invalid order id). request:{}", request);
            throw PaymentException.invalidOrder();
        }

        if (ObjectUtils.isEmpty(request.getSource())) {
            log.warn("Pay-[block]:(invalid source). request:{}", request);
            throw PaymentException.invalidSource();
        }

        OrderResponse order = myOrderById(request.getOrderId());
        Charge charge = paymentService.charge(order.getTotalPay(), request.getSource(), order.getOrderId().toString());

        if (ObjectUtils.isEmpty(charge.getId())) {
            log.warn("Pay-[block]:(invalid chargeId). request:{}", request);
            throw PaymentException.invalid();
        }

        orderService.updateChargeId(order.getOrderId(), charge.getId());
        return PayOrderResponse.builder()
                .orderId(order.getOrderId())
                .url(charge.getAuthorizeUri())
                .build();
    }

    public void updateOrderFromWebhook(Event<Charge> request) {
        if (request.getKey().equals("charge.complete")) {
            Charge data = request.getData();
            String orderId = (String) data.getMetadata().get("orderId");
            String chargeId = data.getId();

            try {
                if (ObjectUtils.isEmpty(orderId)) {
                    log.warn("UpdateOrderFromWebhook-[block]:(not order)");
                    throw OrderException.invalid();
                }

                Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));
                if (!orderOptional.isPresent()) {
                    throw OrderException.invalid();
                }
                Order order = orderOptional.get();

                if (order.getStatus() != OrderStatus.PENDING) {
                    log.warn("UpdateOrderFromWebhook-[block]:(status not pending). orderId:{}", order.getId());
                    throw OrderException.invalid();
                }

                if (!order.getId().toString().equals(orderId)) {
                    log.warn("UpdateOrderFromWebhook-[block]:(not orderId). orderId:{}", order.getId());
                    throw OrderException.invalid();
                }

                if (!order.getChargeId().equals(chargeId)) {
                    log.warn("UpdateOrderFromWebhook-[block]:(not chargeId). chargeId:{}", order.getChargeId());
                    throw OrderException.invalid();
                }

                NotificationPayload payload = new NotificationPayload();
                payload.setType(NotificationType.ORDER);
                payload.setFrom(0L);
                payload.setTo(order.getAccount());

                if (data.getStatus().equals(ChargeStatus.Successful)) {
                    orderService.updateStatus(UUID.fromString(orderId), OrderStatus.SUCCESS);
                    payload.setMessage("order is success");
                } else {
                    log.warn("UpdateOrderFromWebhook-[block]:(not successful). orderId:{}", order.getId());
                    orderService.updateStatus(UUID.fromString(orderId), OrderStatus.FAIL);

                    for (OrderItem item : order.getItems()) {
                        orderService.remainQuantity(item.getProductId(), item.getQuantity());
                    }

                    payload.setMessage("order is fail");
                }

                pushNotificationService.dispatchTo(payload);
            } catch (BaseException e) {
                e.printStackTrace();
                log.warn("UpdateOrderFromWebhook-[block]:(exception). data:{}, error:{}", request, e.getMessage());
            }
        } else {
            log.warn("UpdateOrderFromWebhook-[block]:(not charge.complete). data:{}", request);
        }
    }
}
