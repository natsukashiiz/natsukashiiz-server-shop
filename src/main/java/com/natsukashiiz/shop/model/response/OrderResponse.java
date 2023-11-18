package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.common.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class OrderResponse implements Serializable {
    private UUID orderId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time == null ? LocalDateTime.now() : time;
    }
}
