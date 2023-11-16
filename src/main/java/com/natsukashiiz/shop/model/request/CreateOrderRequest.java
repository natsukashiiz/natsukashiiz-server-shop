package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long productId;

    private Integer quantity;
}
