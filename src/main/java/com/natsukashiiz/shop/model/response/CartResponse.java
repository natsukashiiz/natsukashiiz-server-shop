package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setProductId(cart.getProduct().getId());
        response.setProductName(cart.getProduct().getName());
        response.setOptionId(cart.getProductOption().getId());
        response.setOptionName(cart.getProductOption().getName());
        response.setPrice(cart.getProductOption().getPrice());
        response.setQuantity(cart.getQuantity());

        return response;
    }
}
