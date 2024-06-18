package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.exception.VoucherException;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.repository.*;
import com.natsukashiiz.shop.utils.TimeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    public Order findById(UUID orderId) throws BaseException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw OrderException.invalid();
        }
        return orderOptional.get();
    }

    public List<Order> myOrderListByLatest(Account account) {
        return orderRepository.findAllByAccountOrderByCreatedAtDesc(account);
    }

    public List<Order> myOrderListByStatusAndLatest(Account account, OrderStatus status) {
        return orderRepository.findAllByAccountAndStatusOrderByCreatedAtDesc(account, status);
    }


    @Transactional(rollbackOn = BaseException.class)
    public Order create(CreateOrderRequest request, Account account, Address address) throws BaseException {

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
                log.warn("Buy-[block]:(not found product option). req:{}", orderItem);
                throw ProductException.invalid();
            }
            Product product = productOption.getProduct();
            Category category = product.getCategory();

            if (productOption.getQuantity() - orderItem.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of product option). req:{}, product:{}", orderItem, product);
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
                log.warn("Buy-[block]:(Voucher already used). req:{}, voucher:{}", request, voucher);
                throw VoucherException.alreadyUsed();
            }

            long now = Instant.now().toEpochMilli();
            long expiredAt = TimeUtils.toMilli(voucher.getExpiredAt());

            if (now > expiredAt) {
                voucher.setStatus(VoucherStatus.INACTIVE);
                voucherRepository.save(voucher);

                log.warn("Buy-[block]:(Voucher expired). req:{}, voucher:{}", request, voucher);
                throw VoucherException.expired();
            }

            if (Objects.nonNull(voucher.getProduct())) {
                boolean isContain = items.stream().anyMatch(item -> item.getProductId().equals(voucher.getProduct().getId()));
                if (!isContain) {
                    log.warn("Buy-[block]:(Voucher not applicable for this product). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.notUsedForProduct();
                }
            }

            if (Objects.nonNull(voucher.getCategory())) {
                boolean isContain = items.stream().anyMatch(item -> item.getCategoryId().equals(voucher.getCategory().getId()));
                if (!isContain) {
                    log.warn("Buy-[block]:(Voucher not applicable for this category). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.notUsedForCategory();
                }
            }

            if (voucher.getMinOrderPrice() > 0) {
                if (totalPay < voucher.getMinOrderPrice()) {
                    log.warn("Buy-[block]:(Voucher min order price not reached). req:{}, voucher:{}", request, voucher);
                    throw VoucherException.minOrderPrice();
                }
            }

            double totalDiscount;

            if (DiscountType.AMOUNT.equals(voucher.getDiscountType())) {
                totalDiscount = voucher.getDiscount();
            } else if (DiscountType.PERCENT.equals(voucher.getDiscountType())) {
                totalDiscount = Math.min(voucher.getMaxDiscount(), totalPay * voucher.getDiscount() / 100);
            } else {
                log.warn("Buy-[block]:(Voucher discount type not supported). req:{}, voucher:{}", request, voucher);
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

        return order;
    }

    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Transactional
    public void updateChargeId(UUID orderId, String chargeId) {
        orderRepository.updateChargeId(orderId, chargeId);
    }

    @Transactional
    public void updateStatus(UUID orderId, OrderStatus status) {
        orderRepository.updateStatus(orderId, status);
    }

    @Transactional
    public void remainQuantity(Long optionId, Integer quantity) {
        productOptionRepository.increaseQuantity(optionId, quantity);
    }

    public List<OrderItem> findItemByOrder(Order order) {
        return itemRepository.findByOrder(order);
    }
}
