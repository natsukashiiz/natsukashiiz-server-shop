package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponse implements Serializable {
    private UUID orderId;
    private List<OrderItemResponse> items;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private Double totalPay;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String payUrl;
    private Long payExpire;
    private String payMethod;
    private LocalDateTime paidAt;
    private LocalDateTime cancelAt;
    private LocalDateTime createdAt;

    public static OrderResponse build(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderItemResponse::build)
                .collect(Collectors.toList());

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setItems(items);
        response.setFirstName(order.getFirstName());
        response.setLastName(order.getLastName());
        response.setMobile(order.getMobile());
        response.setAddress(order.getAddress());
        response.setTotalPay(order.getTotalPay());
        response.setStatus(order.getStatus());
        response.setPayUrl(order.getPayUrl());
        response.setPayExpire(order.getPayExpire());
        response.setPayMethod(order.getPayMethod());
        response.setPaidAt(order.getPaidAt());
        response.setCancelAt(order.getCancelAt());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }
}
