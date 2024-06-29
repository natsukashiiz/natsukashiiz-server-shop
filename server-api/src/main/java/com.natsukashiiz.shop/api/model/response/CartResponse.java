package com.natsukashiiz.shop.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartResponse {

    private List<CartItemResponse> items;
    private Long countSelected;
    private Integer totalQuantity;
    private Integer totalSelectedQuantity;
    private Double totalPrice;
}
