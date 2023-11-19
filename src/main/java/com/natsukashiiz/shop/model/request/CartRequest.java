package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private Long optionId;
    private Integer quantity;
}
