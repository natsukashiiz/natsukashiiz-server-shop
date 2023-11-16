package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartResponse {

    private ProductResponse product;
    private Integer quantity;
    private Double totalPrice;

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
