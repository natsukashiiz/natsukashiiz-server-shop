package com.natsukashiiz.shop.service;

import co.omise.models.Charge;
import co.omise.models.ChargeStatus;
import co.omise.models.Event;
import co.omise.models.OmiseException;
import com.natsukashiiz.shop.common.*;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.*;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.CheckoutRequest;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import com.natsukashiiz.shop.model.response.*;
import com.natsukashiiz.shop.repository.*;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final CartRepository cartRepository;
    private final AccountVoucherRepository accountVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final AuthService authService;
    private final PushNotificationService pushNotificationService;
    private final OmisePaymentService omisePaymentService;
    private final AddressService addressService;
    private final ProductService productService;
    private final TaskScheduler taskScheduler;
    private final Map<UUID, ScheduledFuture<?>> tasks;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public Order findById(UUID orderId) throws BaseException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            log.warn("FindById-[block]:(order not found). orderId:{}", orderId);
            throw OrderException.invalid();
        }
        return orderOptional.get();
    }

    @Transactional
    public void remainQuantity(Long optionId, int quantity) {
        productOptionRepository.increaseQuantity(optionId, quantity);
    }

    @Transactional
    public void updateStatus(UUID orderId, OrderStatus status) {
        orderRepository.updateStatus(orderId, status);
    }

    public List<OrderItem> findItemByOrder(Order order) {
        return itemRepository.findByOrder(order);
    }

    public List<OrderResponse> queryAllOrderByStatus(String status) throws BaseException {
        Account account = authService.getAccount();

        List<Order> orders;
        if (!Objects.equals(status.toUpperCase(), "ALL")) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findAllByAccountAndStatusOrderByCreatedAtDesc(account, orderStatus);
        } else {
            orders = orderRepository.findAllByAccountOrderByCreatedAtDesc(account);
        }

        return orders.stream()
                .map(OrderResponse::build)
                .collect(Collectors.toList());
    }

    public OrderResponse queryOrderById(String orderId) throws BaseException {
        Account account = authService.getAccount();

        Optional<Order> orderOptional = orderRepository.findByIdAndAccount(UUID.fromString(orderId), account);
        if (!orderOptional.isPresent()) {
            log.warn("QueryOrderById-[block]:(order not found). orderId:{}, accountId:{}", orderId, account.getId());
            throw OrderException.invalid();
        }

        return OrderResponse.build(orderOptional.get());
    }

    public OrderCheckoutResponse checkout(CheckoutRequest request) throws BaseException {
        Account account = authService.getAccount();

        List<Cart> carts = cartRepository.findAllByAccountAndSelectedIsTrue(account);
        if (carts.isEmpty()) {
            log.warn("Checkout-[block]:(cart selected is empty). request:{}, accountId:{}", request, account.getId());
            throw CartException.selectedEmpty();
        }

        OrderCheckoutResponse response = new OrderCheckoutResponse();

        Optional<Address> addressOptional = addressRepository.findByAccountAndMainIsTrue(account);
        if (!addressOptional.isPresent()) {
            log.warn("Checkout-[block]:(address not found). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }
        Address address = addressOptional.get();

        response.setAddress(AddressResponse.build(address));
        response.setTotalDiscount(0.0);
        response.setTotalQuantity(0);
        response.setTotalPay(0.0);

        List<OrderCheckoutResponse.OrderCheckoutItem> items = new ArrayList<>();

        for (Cart cart : carts) {
            Product product = cart.getProduct();
            ProductOption option = cart.getProductOption();

            if (option.getQuantity() - cart.getQuantity() < 0) {
                log.warn("Checkout-[block]:(insufficient quantity). request:{}, productId:{}, optionId:{}, accountId:{}", request, product.getId(), option.getId(), account.getId());
                throw ProductException.insufficient();
            }

            double totalPrice = option.getPrice() * cart.getQuantity();
            response.setTotalPay(response.getTotalPay() + totalPrice);

            OrderCheckoutResponse.OrderCheckoutItem item = new OrderCheckoutResponse.OrderCheckoutItem();
            item.setProduct(ProductResponse.build(product));
            item.setOption(ProductOptionResponse.build(option));
            item.setQuantity(cart.getQuantity());
            items.add(item);

            response.setTotalQuantity(response.getTotalQuantity() + item.getQuantity());
        }
        response.setItems(items);

        if (!ObjectUtils.isEmpty(request.getVoucherId())) {
            Optional<Voucher> voucherOptional = voucherRepository.findById(request.getVoucherId());
            if (!voucherOptional.isPresent()) {
                log.warn("Checkout-[block]:(voucher not found). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.invalid();
            }
            Voucher voucher = voucherOptional.get();
            AccountVoucher accountVoucher = accountVoucherRepository.findByAccountAndVoucher(account, voucher).orElseThrow(VoucherException::notClaimed);

            if (accountVoucher.getUsed()) {
                log.warn("Checkout-[block]:(voucher already used). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.alreadyUsed();
            }

            long now = Instant.now().toEpochMilli();
            long expiredAt = TimeUtils.toMilli(voucher.getExpiredAt());

            if (now > expiredAt) {
                voucher.setStatus(VoucherStatus.INACTIVE);
                voucherRepository.save(voucher);

                log.warn("Checkout-[block]:(voucher expired). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.expired();
            }

            if (Objects.nonNull(voucher.getProduct())) {
                boolean isContain = items.stream().anyMatch(item -> item.getProduct().getId().equals(voucher.getProduct().getId()));
                if (!isContain) {
                    log.warn("Checkout-[block]:(voucher not applicable for this product). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.notUsedForProduct();
                }
            }

            if (Objects.nonNull(voucher.getCategory())) {
                boolean isContain = items.stream().anyMatch(item -> item.getProduct().getCategory().getId().equals(voucher.getCategory().getId()));
                if (!isContain) {
                    log.warn("Checkout-[block]:(voucher not applicable for this category). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.notUsedForCategory();
                }
            }

            if (voucher.getMinOrderPrice() > 0) {
                if (response.getTotalPay() < voucher.getMinOrderPrice()) {
                    log.warn("Checkout-[block]:(voucher min order price not reached). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.minOrderPrice();
                }
            }

            double totalDiscount;

            if (DiscountType.AMOUNT.equals(voucher.getDiscountType())) {
                totalDiscount = voucher.getDiscount();
            } else if (DiscountType.PERCENT.equals(voucher.getDiscountType())) {
                totalDiscount = Math.min(voucher.getMaxDiscount(), (response.getTotalPay() * voucher.getDiscount()) / 100);
            } else {
                log.warn("Checkout-[block]:(Voucher discount type not supported). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.invalid();
            }

            response.setTotalDiscount(totalDiscount);
        }

        response.setVouchers(accountVoucherRepository.findAllByAccountAndUsedIsFalseAndVoucherStatusAndVoucherMinOrderPriceLessThanEqual(account, VoucherStatus.ACTIVE, response.getTotalPay())
                .stream()
                .filter(e -> {
                    if (Objects.nonNull(e.getVoucher().getProduct())) {
                        return items.stream().anyMatch(item -> item.getProduct().getId().equals(e.getVoucher().getProduct().getId()));
                    } else if (Objects.nonNull(e.getVoucher().getCategory())) {
                        return items.stream().anyMatch(item -> item.getProduct().getCategory().getId().equals(e.getVoucher().getCategory().getId()));
                    } else {
                        return true;
                    }
                })
                .map(e -> VoucherResponse.build(e.getVoucher()))
                .collect(Collectors.toList()));

        response.setActualPay(Math.max(1, response.getTotalPay() - response.getTotalDiscount()));
        response.setTotalShipping(0.0);

        return response;
    }

    @Transactional(rollbackOn = BaseException.class)
    public OrderResponse createOrder(CreateOrderRequest request) throws BaseException {
        Account account = authService.getAccount();

        Optional<Address> addressOptional = addressRepository.findByAccountAndMainIsTrue(authService.getAccount());
        if (!addressOptional.isPresent()) {
            log.warn("Buy-[block]:(main address not found). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        Address address = addressOptional.get();
        List<CreateOrderRequest.OrderItem> orderItems = request.getOrderItems();
        Map<Long, ProductOption> productOptionMap = productOptionRepository.findAllById(orderItems.stream().map(CreateOrderRequest.OrderItem::getOptionId).collect(Collectors.toSet())).stream().collect(Collectors.toMap(ProductOption::getId, Function.identity()));

        Order order = new Order();
        order.setAccount(account);
        order.setFirstName(address.getFirstName());
        order.setLastName(address.getLastName());
        order.setMobile(address.getMobile());
        order.setAddress(address.getAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setPayExpire(Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli());

        List<OrderItem> items = new LinkedList<>();

        double totalPay = 0.0;
        for (CreateOrderRequest.OrderItem orderItem : orderItems) {
            ProductOption productOption = productOptionMap.get(orderItem.getOptionId());
            if (Objects.isNull(productOption)) {
                log.warn("Buy-[block]:(not found product option). request:{}, accountId:{}", request, account.getId());
                throw ProductException.invalid();
            }
            Product product = productOption.getProduct();
            Category category = product.getCategory();

            if (productOption.getQuantity() - orderItem.getQuantity() < 0) {
                log.warn("Buy-[block]:(insufficient quantity of product option). request:{}, productId:{}, optionId:{}, accountId:{}", request, product.getId(), productOption.getId(), account.getId());
                throw ProductException.insufficient();
            }

            productOptionRepository.decreaseQuantity(productOption.getId(), orderItem.getQuantity());
            cartRepository.deleteByProductOptionAndAccount(productOption, account);

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
                log.warn("Buy-[block]:(voucher already used). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.alreadyUsed();
            }

            long now = Instant.now().toEpochMilli();
            long expiredAt = TimeUtils.toMilli(voucher.getExpiredAt());

            if (now > expiredAt) {
                voucher.setStatus(VoucherStatus.INACTIVE);
                voucherRepository.save(voucher);

                log.warn("Buy-[block]:(voucher expired). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.expired();
            }

            if (Objects.nonNull(voucher.getProduct())) {
                boolean isContain = items.stream().anyMatch(item -> item.getProductId().equals(voucher.getProduct().getId()));
                if (!isContain) {
                    log.warn("Buy-[block]:(voucher not applicable for this product). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.notUsedForProduct();
                }
            }

            if (Objects.nonNull(voucher.getCategory())) {
                boolean isContain = items.stream().anyMatch(item -> item.getCategoryId().equals(voucher.getCategory().getId()));
                if (!isContain) {
                    log.warn("Buy-[block]:(voucher not applicable for this category). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.notUsedForCategory();
                }
            }

            if (voucher.getMinOrderPrice() > 0) {
                if (totalPay < voucher.getMinOrderPrice()) {
                    log.warn("Buy-[block]:(voucher min order price not reached). request:{}, accountId:{}", request, account.getId());
                    throw VoucherException.minOrderPrice();
                }
            }

            double totalDiscount;

            if (DiscountType.AMOUNT.equals(voucher.getDiscountType())) {
                totalDiscount = voucher.getDiscount();
            } else if (DiscountType.PERCENT.equals(voucher.getDiscountType())) {
                totalDiscount = Math.min(voucher.getMaxDiscount(), totalPay * voucher.getDiscount() / 100);
            } else {
                log.warn("Buy-[block]:(voucher discount type not supported). request:{}, accountId:{}", request, account.getId());
                throw VoucherException.invalid();
            }

            totalPay -= totalDiscount;
            if (totalPay <= 0) {
                totalPay = 1;
            }

            order.setTotalDiscount(voucher.getDiscount());

            accountVoucher.setUsed(Boolean.TRUE);
            accountVoucherRepository.save(accountVoucher);
        }

        order.setActualPay(totalPay);

        orderRepository.save(order);
        itemRepository.saveAll(items);

        order.setItems(items);

        // time out task
//        ScheduledFuture<?> schedule = taskScheduler.schedule(
//                new OrderExpireTask(order.getId(), orderService),
//                new Date(order.getPayExpire())
//        );
//        tasks.put(order.getId(), schedule);

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
        pushNotificationService.pushTo(payload);

        return OrderResponse.build(order);
    }

    @Transactional(rollbackOn = BaseException.class)
    public PayOrderResponse payOrder(PayOrderRequest request) throws BaseException {
        Account account = authService.getAccount();

        if (ObjectUtils.isEmpty(request.getOrderId())) {
            log.warn("PayOrder-[block]:(invalid order id). request:{}, accountId:{}", request, account.getId());
            throw PaymentException.invalidOrder();
        }

        if (ObjectUtils.isEmpty(request.getSource())) {
            log.warn("PayOrder-[block]:(invalid source). request:{}, accountId:{}", request, account.getId());
            throw PaymentException.invalidSource();
        }

        Optional<Order> orderOptional = orderRepository.findByIdAndAccount(UUID.fromString(request.getOrderId()), account);
        if (!orderOptional.isPresent()) {
            log.warn("PayOrder-[block]:(order not found). request:{}, accountId:{}", request, account.getId());
            throw OrderException.invalid();
        }

        Order order = orderOptional.get();

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("PayOrder-[block]:(status not pending). request:{}, accountId:{}", request, account.getId());
            throw OrderException.invalid();
        }

        try {
            Charge charge = omisePaymentService.charge(order.getTotalPay(), request.getSource(), order.getId(), order.getPayExpire());

            if (ObjectUtils.isEmpty(charge.getId())) {
                log.warn("PayOrder-[block]:(invalid chargeId). request:{}, accountId:{}", request, account.getId());
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
            orderRepository.save(order);

            tasks.remove(order.getId());
            return PayOrderResponse.build(order.getId(), type, url);
        } catch (IOException | OmiseException e) {
            if (e instanceof OmiseException) {
                log.warn("PayOrder-[block]:(omise exception). request:{}, error:{}", request, e.getMessage());
            } else {
                log.warn("PayOrder-[block]:(io exception). request:{}, error:{}", request, e.getMessage());
            }

            throw PaymentException.invalid();
        }
    }

    @Transactional(rollbackOn = BaseException.class)
    public OrderResponse cancelOrder(String orderId) throws BaseException {
        Account account = authService.getAccount();

        if (ObjectUtils.isEmpty(orderId)) {
            log.warn("CancelOrder-[block]:(invalid order id). orderId:{}, accountId:{}", orderId, account.getId());
            throw PaymentException.invalidOrder();
        }

        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(OrderException::invalid);

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("CancelOrder-[block]:(status not pending). orderId:{}, accountId:{}", orderId, account.getId());
            throw OrderException.invalid();
        }

        for (OrderItem item : order.getItems()) {
            productOptionRepository.increaseQuantity(item.getOptionId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.SELF_CANCELED);
        order.setCancelAt(LocalDateTime.now());
        orderRepository.save(order);

        tasks.remove(order.getId());

        return OrderResponse.build(order);
    }

    @Transactional
    public void updateOrderFromWebhook(Event<Charge> request) {
        if (StringUtils.hasText(request.getKey())) {
            Charge data = request.getData();
            String chargeId = data.getId();
            if (request.getKey().equals("charge.complete")) {
                String orderId = (String) data.getMetadata().get("orderId");

                try {
                    if (ObjectUtils.isEmpty(orderId)) {
                        log.warn("UpdateOrderFromWebhook-[block]:(order id empty). chargeId:{}", chargeId);
                        throw OrderException.invalid();
                    }

                    Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));
                    if (!orderOptional.isPresent()) {
                        log.warn("UpdateOrderFromWebhook-[block]:(order not found). chargeId:{}, orderId:{}", chargeId, orderId);
                        throw OrderException.invalid();
                    }
                    Order order = orderOptional.get();

                    if (order.getStatus() != OrderStatus.PENDING) {
                        log.warn("UpdateOrderFromWebhook-[block]:(status not pending). chargeId:{}, orderId:{}", chargeId, orderId);
                        throw OrderException.invalid();
                    }

                    if (!order.getId().toString().equals(orderId)) {
                        log.warn("UpdateOrderFromWebhook-[block]:(not orderId). chargeId:{}, orderId:{}", chargeId, orderId);
                        throw OrderException.invalid();
                    }

                    if (!order.getChargeId().equals(chargeId)) {
                        log.warn("UpdateOrderFromWebhook-[block]:(not chargeId). chargeId:{}, orderId:{}", chargeId, orderId);
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
                        orderRepository.save(order);
                        notify.setContent("ชำระเงินสำเร็จ คำสั่งซื้อหมายเลข " + order.getId());

                        // update amount of orders
                        List<Product> update = new ArrayList<>();
                        for (OrderItem item : order.getItems()) {

                            Optional<Product> productOptional = productRepository.findById(item.getProductId());
                            if (!productOptional.isPresent()) {
                                log.warn("UpdateOrderFromWebhook-[block]:(product not found). chargeId:{}, orderId:{}, productId:{}", chargeId, orderId, item.getProductId());
                                throw ProductException.invalid();
                            }
                            Product product = productOptional.get();

                            product.setOrders(product.getOrders() + item.getQuantity());
                            update.add(product);
                        }
                        productRepository.saveAll(update);
                    } else {
                        log.warn("UpdateOrderFromWebhook-[block]:(fail). chargeId:{}, orderId:{}", chargeId, orderId);
                        orderRepository.updateStatus(UUID.fromString(orderId), OrderStatus.FAILED);

                        for (OrderItem item : order.getItems()) {
                            productOptionRepository.increaseQuantity(item.getOptionId(), item.getQuantity());
                        }

                        notify.setContent("ชำระเงินล้มเหลว คำสั่งซื้อหมายเลข " + order.getId());
                    }

                    notify.setType(order.getStatus());
                    payload.setNotification(notify);
                    pushNotificationService.pushTo(payload);
                } catch (BaseException e) {
                    log.warn("UpdateOrderFromWebhook-[block]:(exception). chargeId:{}, orderId:{}", chargeId, orderId, e);
                }
            } else if (request.getKey().equals("charge.create")) {
                log.debug("UpdateOrderFromWebhook-[next]:(charge create). chargeId:{}", chargeId);
            } else {
                log.warn("UpdateOrderFromWebhook-[block]:(not charge.complete). chargeId:{}", chargeId);
            }
        } else {
            log.warn("UpdateOrderFromWebhook-[block]:(key empty). data:{}", request);
        }
    }
}
