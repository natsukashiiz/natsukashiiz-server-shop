package com.natsukashiiz.shop.model.request;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class BuyRequest {
    @Positive
    private Long productId;

    @Positive
    private Integer quantity;
}
