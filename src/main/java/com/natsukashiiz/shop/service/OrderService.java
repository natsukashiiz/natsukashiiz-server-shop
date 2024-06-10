package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.repository.CartRepository;
import com.natsukashiiz.shop.repository.OrderItemRepository;
import com.natsukashiiz.shop.repository.OrderRepository;
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
    public Order create(List<CreateOrderRequest> requests, Account account, Address address) throws BaseException {

        Map<Long, ProductOption> productOptionMap = productOptionRepository.findAllById(requests.stream().map(CreateOrderRequest::getOptionId).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));

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
        for (CreateOrderRequest req : requests) {
            ProductOption productOption = productOptionMap.get(req.getOptionId());
            if (Objects.isNull(productOption)) {
                log.warn("Buy-[block]:(not found product option). req:{}", req);
                throw ProductException.invalid();
            }
            Product product = productOption.getProduct();

            if (productOption.getQuantity() - req.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of product option). req:{}, product:{}", req, product);
                throw ProductException.insufficient();
            }

            productOptionRepository.decreaseQuantity(productOption.getId(), req.getQuantity());
            cartRepository.deleteByProductOptionAndAccount(productOption, account);

            double totalPrice = productOption.getPrice() * req.getQuantity();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductThumbnail(product.getImages().get(0).getUrl());
            item.setOptionId(productOption.getId());
            item.setOptionName(productOption.getName());
            item.setPrice(productOption.getPrice());
            item.setQuantity(req.getQuantity());
            item.setTotalPrice(totalPrice);

            items.add(item);

            totalPay += totalPrice;
        }

        order.setTotalPay(totalPay);
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
