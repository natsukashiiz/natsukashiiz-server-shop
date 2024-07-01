package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Order}
 */
@Getter
@Setter
@ToString
public class OrderDTO implements Serializable {
    private UUID id;
    private UserDTO user;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private List<OrderItemDTO> items;
    private Double totalPay;
    private Double totalDiscount;
    private Double actualPay;
    private OrderStatus status;
    private String chargeId;
    private String payUrl;
    private Long payExpire;
    private String payMethod;
    private LocalDateTime paidAt;
    private LocalDateTime cancelAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDTO fromEntity(Order order) {
        OrderDTO response = new OrderDTO();
        response.setId(order.getId());
        response.setUser(UserDTO.fromEntity(order.getUser()));
        response.setFirstName(order.getFirstName());
        response.setLastName(order.getLastName());
        response.setMobile(order.getMobile());
        response.setAddress(order.getAddress());
        response.setItems(order.getItems().stream().map(OrderItemDTO::fromEntity).collect(Collectors.toList()));
        response.setTotalPay(order.getTotalPay());
        response.setTotalDiscount(order.getTotalDiscount());
        response.setActualPay(order.getActualPay());
        response.setStatus(order.getStatus());
        response.setChargeId(order.getChargeId());
        response.setPayUrl(order.getPayUrl());
        response.setPayExpire(order.getPayExpire());
        response.setPayMethod(order.getPayMethod());
        response.setPaidAt(order.getPaidAt());
        response.setCancelAt(order.getCancelAt());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }

    public Order toEntity() {
        Order order = new Order();
        order.setId(id);
        order.setUser(user.toEntity());
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setMobile(mobile);
        order.setAddress(address);
        order.setItems(items.stream().map(OrderItemDTO::toEntity).collect(Collectors.toList()));
        order.setTotalPay(totalPay);
        order.setTotalDiscount(totalDiscount);
        order.setActualPay(actualPay);
        order.setStatus(status);
        order.setChargeId(chargeId);
        order.setPayUrl(payUrl);
        order.setPayExpire(payExpire);
        order.setPayMethod(payMethod);
        order.setPaidAt(paidAt);
        order.setCancelAt(cancelAt);
        order.setCreatedAt(createdAt);
        order.setUpdatedAt(updatedAt);
        return order;
    }
}