package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;

    @Cacheable(value = "myOrders", key = "#account.id")
    public List<OrderResponse> myOrders(Account account) {
        return orderRepository.findAllByAccount(account)
                .stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .productName(order.getProductName())
                        .productPrice(order.getPrice())
                        .quantity(order.getQuantity())
                        .totalPrice(order.getTotalPrice())
                        .time(order.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
