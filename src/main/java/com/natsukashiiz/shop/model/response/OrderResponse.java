package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class OrderResponse {
    private UUID orderId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime time;
}
