package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.OrderException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.repository.CartRepository;
import com.natsukashiiz.shop.repository.OrderRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public Order myOrderById(UUID orderId) throws BaseException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw OrderException.invalid();
        }
        return orderOptional.get();
    }

    public List<Order> myOrderListByLatest(Account account) {
        return orderRepository.findAllByAccountOrderByCreatedAtDesc(account);
    }


    @Transactional(rollbackOn = BaseException.class)
    public List<Order> create(List<CreateOrderRequest> requests, Account account) throws BaseException {

        Map<Long, Product> productMap = productRepository.findAllById(requests.stream().map(CreateOrderRequest::getProductId).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));


        List<Order> responses = new LinkedList<>();
        for (CreateOrderRequest req : requests) {
            Product product = productMap.get(req.getProductId());
            if (Objects.isNull(product)) {
                log.warn("Buy-[block]:(not found product). req:{}", req);
                throw ProductException.invalid();
            }

            if (product.getQuantity() - req.getQuantity() < 0) {
                log.warn("Buy-[block]:(Insufficient quantity of products). req:{}, product:{}", req, product);
                throw ProductException.insufficient();
            }

            productRepository.decreaseQuantity(product.getId(), req.getQuantity());
            cartRepository.deleteByProductAndAccount(product, account);

            double totalPrice = product.getPrice() * req.getQuantity();

            Order order = new Order();
            order.setAccount(account);
            order.setProductId(product.getId());
            order.setProductName(product.getName());
            order.setPrice(product.getPrice());
            order.setQuantity(req.getQuantity());
            order.setTotalPrice(totalPrice);
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);

            responses.add(order);
        }

        return responses;
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
    public void remainQuantity(Long productId, Integer quantity) {
        productRepository.increaseQuantity(productId, quantity);
    }
}
