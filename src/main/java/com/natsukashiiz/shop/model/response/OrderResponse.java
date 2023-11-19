package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Order;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
public class OrderResponse implements Serializable {
    private UUID orderId;
    private List<OrderItemResponse> items;
    private Double totalPay;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    private Timestamp time;

    public Timestamp getTime() {
        return time == null ? Timestamp.from(Instant.now()) : time;
    }

    public static OrderResponse build(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderItemResponse::build)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .orderId(order.getId())
                .items(items)
                .status(order.getStatus())
                .totalPay(order.getTotalPay())
                .time(order.getCreatedAt())
                .build();
    }
}
