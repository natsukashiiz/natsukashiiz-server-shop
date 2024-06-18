package com.natsukashiiz.shop.business;

import co.omise.models.Charge;
import co.omise.models.ChargeStatus;
import co.omise.models.Event;
import co.omise.models.OmiseException;
import com.natsukashiiz.shop.common.*;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.*;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.PayOrderResponse;
import com.natsukashiiz.shop.payment.PaymentService;
import com.natsukashiiz.shop.repository.AccountVoucherRepository;
import com.natsukashiiz.shop.repository.OrderRepository;
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import com.natsukashiiz.shop.repository.VoucherRepository;
import com.natsukashiiz.shop.service.*;
import com.natsukashiiz.shop.task.OrderExpireTask;
import com.natsukashiiz.shop.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Function;
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
    private final ProductOptionRepository productOptionRepository;
    private final VoucherRepository voucherRepository;
    private final AccountVoucherRepository accountVoucherRepository;

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

    public OrderResponse checkout(CreateOrderRequest request) throws BaseException {
        Account account = authService.getCurrent();
        Order order = new Order();

        Address address = addressService.getMain(account);
        order.setFirstName(address.getFirstName());
        order.setLastName(address.getLastName());
        order.setMobile(address.getMobile());
        order.setAddress(address.getAddress());

        List<CreateOrderRequest.OrderItem> orderItems = request.getOrderItems();
        Map<Long, ProductOption> productOptionMap = productOptionRepository.findAllById(orderItems.stream().map(CreateOrderRequest.OrderItem::getOptionId).collect(Collectors.toSet())).stream().collect(Collectors.toMap(ProductOption::getId, Function.identity()));

        order.setAccount(account);
        order.setPayExpire(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli());

        List<OrderItem> items = new LinkedList<>();

        double totalPay = 0.0;
        for (CreateOrderRequest.OrderItem orderItem : orderItems) {
            ProductOption productOption = productOptionMap.get(orderItem.getOptionId());
            if (Objects.isNull(productOption)) {
                log.warn("Checkout-[block]:(not found product option). req:{}", orderItem);
                throw ProductException.invalid();
            }
            Product product = productOption.getProduct();
            Category category = product.getCategory();

            if (productOption.getQuantity() - orderItem.getQuantity() < 0) {
                log.warn("Checkout-[block]:(Insufficient quantity of product option). req:{}, product:{}", orderItem, product);
                throw ProductException.insufficient();
            }

            double totalPrice = productOption.getPrice() * orderItem.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductThumbnail(product.getImages().get(0).getUrl());
            item.setOptionId(productOption.getId());
            item.setOptionName(productOption.getName());
            item.setCategoryId(category.getId());
            item.setCategoryName(category.getName());
            item.setPrice(productOption.getPrice());
            item.setQuantity(orderItem.getQuantity());
            item.setTotalPrice(totalPrice);

            items.add(item);

            totalPay += totalPrice;
        }

        order.setTotalPay(totalPay);

        if (!ObjectUtils.isEmpty(request.getVoucherId())) {
            Voucher voucher = voucherRepository.findById(request.getVoucherId()).orElseThrow(VoucherException::invalid);
            AccountVoucher accountVoucher = accountVoucherRepository.findByAccountAndVoucher(account, voucher).orElseThrow(VoucherException::notClaimed);

            if (accountVoucher.getUsed()) {
                log.warn("Checkout-[block]:(Voucher already used). req:{}, voucher:{}", request, voucher);
                throw VoucherException.alreadyUsed();
            }

            long now = Instant.now().toEpochMilli();
            long expiredAt = TimeUtils.toMilli(voucher.getExpiredAt());

            if (now > expiredAt) {
                voucher.setStatus(VoucherStatus.INACTIVE);
                voucherRepository.save(voucher);

                log.warn("Checkout-[block]:(Voucher expired). req:{}, voucher:{}", request, voucher);
                throw VoucherException.expired();
            }

            if (Objects.nonNull(voucher.getProduct())) {
                boolean isContain = items.stream().anyMatch(item -> item.getProductId().equals(voucher.getProduct().getId()));
                if (!isContain) {
                    log.warn("Checkout-[block]:(Voucher not applicable for this product). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.notUsedForProduct();
                }
            }

            if (Objects.nonNull(voucher.getCategory())) {
                boolean isContain = items.stream().anyMatch(item -> item.getCategoryId().equals(voucher.getCategory().getId()));
                if (!isContain) {
                    log.warn("Checkout-[block]:(Voucher not applicable for this category). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.notUsedForCategory();
                }
            }

            if (voucher.getMinOrderPrice() > 0) {
                if (totalPay < voucher.getMinOrderPrice()) {
                    log.warn("Checkout-[block]:(Voucher min order price not reached). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.minOrderPrice();
                }
            }

            double totalDiscount;

            if (DiscountType.AMOUNT.equals(voucher.getDiscountType())) {
                totalDiscount = voucher.getDiscount();
            } else if (DiscountType.PERCENT.equals(voucher.getDiscountType())) {
                totalDiscount = Math.min(voucher.getMaxDiscount(), totalPay * voucher.getDiscount() / 100);
            } else {
                log.warn("Checkout-[block]:(Voucher discount type not supported). req:{}, voucher:{}", request, voucher);
                throw VoucherException.invalid();
            }

            totalPay -= totalDiscount;
            if (totalPay <= 0) {
                totalPay = 1;
            }

            order.setTotalDiscount(voucher.getDiscount());
        }

        order.setActualPay(totalPay);
        order.setItems(items);

        return OrderResponse.build(order);
    }

    public OrderResponse create(CreateOrderRequest request) throws BaseException {
        Address address = addressService.getMain(authService.getCurrent());
        Order order = orderService.create(request, authService.getCurrent(), address);

        // time out task
        ScheduledFuture<?> schedule = taskScheduler.schedule(
                new OrderExpireTask(order.getId(), orderService),
                new Date(order.getPayExpire())
        );
        tasks.put(order.getId(), schedule);

        NotificationPayload payload = new NotificationPayload();
        payload.setAccount(order.getAccount());
        payload.setType(NotificationType.ORDER);

        Notification notify = new Notification();
        notify.setAccount(order.getAccount());
        notify.setType(order.getStatus());
        notify.setEventId(order.getId().toString());
        notify.setTitle("กรุณาชำระเงิน");
        notify.setContent("กรุณาชำระเงิน คำสั่งซื้อหมายเลข " + order.getId());
        notify.setIsRead(Boolean.FALSE);

        payload.setNotification(notify);
        pushNotificationService.dispatchTo(payload);

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

        try {
            Charge charge = paymentService.charge(order.getTotalPay(), request.getSource(), order.getId(), order.getPayExpire());

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
        } catch (IOException | OmiseException e) {
            if (e instanceof OmiseException) {
                log.warn("Pay-[block]:(omise exception). request:{}, error:{}", request, e.getMessage());
            } else {
                log.warn("Pay-[block]:(io exception). request:{}, error:{}", request, e.getMessage());
            }

            throw PaymentException.invalid();
        }
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
                        log.warn("UpdateOrderFromWebhook-[block]:(order id empty)");
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
                    payload.setAccount(order.getAccount());
                    payload.setType(NotificationType.ORDER);

                    Notification notify = new Notification();
                    notify.setAccount(order.getAccount());
                    notify.setEventId(order.getId().toString());
                    notify.setTitle("การชำระเงิน");
                    notify.setIsRead(Boolean.FALSE);

                    if (data.getStatus().equals(ChargeStatus.Successful)) {
                        order.setStatus(OrderStatus.PAID);
                        orderService.update(order);
                        notify.setContent("ชำระเงินสำเร็จ คำสั่งซื้อหมายเลข " + order.getId());

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

                        notify.setContent("ชำระเงินล้มเหลว คำสั่งซื้อหมายเลข " + order.getId());
                    }

                    notify.setType(order.getStatus());
                    payload.setNotification(notify);
                    pushNotificationService.dispatchTo(payload);
                } catch (BaseException e) {
                    e.printStackTrace();
                    log.warn("UpdateOrderFromWebhook-[block]:(exception). data:{}, error:{}", request, e.getMessage());
                }
            } else if (request.getKey().equals("charge.create")) {
                log.debug("UpdateOrderFromWebhook-[next]:(charge create)");
            } else {
                log.warn("UpdateOrderFromWebhook-[block]:(not charge.complete). data:{}", request);
            }
        }
    }
}
