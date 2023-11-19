package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.Product;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Long optionId;
    private String optionName;
    private Double price;
    private Integer quantity;
    private Product product;

    public static CartResponse build(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .productId(cart.getId())
                .productName(cart.getProduct().getName())
                .optionId(cart.getProductOption().getId())
                .optionName(cart.getProductOption().getName())
                .price(cart.getProductOption().getPrice())
                .quantity(cart.getQuantity())
//                .product(cart.getProduct())
                .build();
    }
}
