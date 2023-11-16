package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class OrderResponse implements Serializable {
    private UUID orderId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time == null ? LocalDateTime.now() : time;
    }
}
