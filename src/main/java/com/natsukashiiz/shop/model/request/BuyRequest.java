package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class BuyRequest {
    private Long productId;

    private Integer quantity;
}
