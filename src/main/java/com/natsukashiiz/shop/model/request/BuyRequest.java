package com.natsukashiiz.shop.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@Data
public class BuyRequest {
    @Positive
    private Long productId;

    @Min(1)
    private Integer quantity;
}
