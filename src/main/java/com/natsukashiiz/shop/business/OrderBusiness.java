package com.natsukashiiz.shop.business;

import co.omise.models.Charge;
import co.omise.models.ChargeStatus;
import co.omise.models.Event;
import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.common.PayUrlType;
import com.natsukashiiz.shop.entity.Address;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.OrderItem;
import com.natsukashiiz.shop.entity.Product;
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
import com.natsukashiiz.shop.service.*;
import com.natsukashiiz.shop.task.OrderExpireTask;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
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
    private final AddressService addressService;
    private final ProductService productService;
    private final TaskScheduler taskScheduler;
    private final Map<UUID, ScheduledFuture<?>> tasks;

    public List<OrderResponse> myOrders(String status) throws BaseException {

        List<Order> list;
        if (!Objects.equals(status.toUpperCase(), "ALL")) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            list = orderService.myOrderListByStatusAndLatest(authService.getCurrent(), orderStatus);
        } else {
            list = orderService.myOrderListByLatest(authService.getCurrent());
        }

        return list.stream()
                .map(OrderResponse::build)
                .collect(Collectors.toList());
    }

    public OrderResponse myOrderById(String orderId) throws BaseException {
        return OrderResponse.build(orderService.findById(UUID.fromString(orderId)));
    }

    public OrderResponse create(List<CreateOrderRequest> requests) throws BaseException {
        Address address = addressService.getMain(authService.getCurrent());
        Order order = orderService.create(requests, authService.getCurrent(), address);

        // time out task
        ScheduledFuture<?> schedule = taskScheduler.schedule(
                new OrderExpireTask(order.getId(), orderService),
                new Date(order.getPayExpire())
        );
        tasks.put(order.getId(), schedule);

        NotificationPayload notify = new NotificationPayload();
        notify.setType(NotificationType.ORDER);
        notify.setTo(authService.getCurrent());
        notify.setMessage("you have new order no " + order.getId());
        pushNotificationService.dispatchTo(notify);

        return OrderResponse.build(order);
    }

    @Transactional(rollbackOn = BaseException.class)
    public PayOrderResponse pay(PayOrderRequest request) throws BaseException {

        if (ObjectUtils.isEmpty(request.getOrderId())) {
            log.warn("Pay-[block]:(invalid order id). request:{}", request);
            throw PaymentException.invalidOrder();
        }

        if (ObjectUtils.isEmpty(request.getSource())) {
            log.warn("Pay-[block]:(invalid source). request:{}", request);
            throw PaymentException.invalidSource();
        }

        Order order = orderService.findById(UUID.fromString(request.getOrderId()));

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Pay-[block]:(status not pending). request:{}", request);
            throw OrderException.invalid();
        }

        Charge charge = paymentService.charge(order.getTotalPay(), request.getSource(), order.getId());

        if (ObjectUtils.isEmpty(charge.getId())) {
            log.warn("Pay-[block]:(invalid chargeId). request:{}", request);
            throw PaymentException.invalid();
        }

        String url;
        PayUrlType type;

        if (ObjectUtils.isEmpty(charge.getSource().getScannableCode())) {
            url = charge.getAuthorizeUri();
            type = PayUrlType.LINK;
        } else {
            url = charge.getSource().getScannableCode().getImage().getDownloadUri();
            type = PayUrlType.IMAGE;
        }

        order.setChargeId(charge.getId());
        order.setPayMethod(charge.getSource().getType().toString());
        order.setPayUrl(url);
        order.setPaidAt(LocalDateTime.now());
        orderService.update(order);

        tasks.remove(order.getId());
        return PayOrderResponse.build(order.getId(), type, url);
    }

    @Transactional(rollbackOn = BaseException.class)
    public OrderResponse cancel(String orderId) throws BaseException {

        if (ObjectUtils.isEmpty(orderId)) {
            log.warn("Cancel-[block]:(invalid order id). orderId:{}", orderId);
            throw PaymentException.invalidOrder();
        }

        Order order = orderService.findById(UUID.fromString(orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Cancel-[block]:(status not pending). orderId:{}", orderId);
            throw OrderException.invalid();
        }

        for (OrderItem item : order.getItems()) {
            orderService.remainQuantity(item.getProductId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.SELF_CANCELED);
        order.setCancelAt(LocalDateTime.now());
        orderService.update(order);

        tasks.remove(order.getId());

        return OrderResponse.build(order);
    }

    @Transactional
    public void updateOrderFromWebhook(Event<Charge> request) {
        if (StringUtils.hasText(request.getKey())) {
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
                        order.setStatus(OrderStatus.PAID);
                        orderService.update(order);
                        payload.setMessage("order is success");

                        // update amount of orders
                        List<Product> update = new ArrayList<>();
                        for (OrderItem item : order.getItems()) {
                            Product product = productService.getById(item.getProductId());
                            product.setOrders(product.getOrders() + item.getQuantity());
                            update.add(product);
                        }
                        productService.createOrUpdateAll(update);
                    } else {
                        log.warn("UpdateOrderFromWebhook-[block]:(fail). orderId:{}", order.getId());
                        orderService.updateStatus(UUID.fromString(orderId), OrderStatus.FAILED);

                        for (OrderItem item : order.getItems()) {
                            orderService.remainQuantity(item.getOptionId(), item.getQuantity());
                        }

                        payload.setMessage("order is fail");
                    }
                    pushNotificationService.dispatchTo(payload);
                } catch (BaseException e) {
                    e.printStackTrace();
                    log.warn("UpdateOrderFromWebhook-[block]:(exception). data:{}, error:{}", request, e.getMessage());
                }
            } else if (request.getKey().equals("charge.create")) {
                log.warn("UpdateOrderFromWebhook-[next]:(charge create)");
            } else {
                log.warn("UpdateOrderFromWebhook-[block]:(not charge.complete). data:{}", request);
            }
        }
    }
}
