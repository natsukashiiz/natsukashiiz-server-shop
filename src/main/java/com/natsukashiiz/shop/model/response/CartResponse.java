package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Cart;
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
    private Integer maxQuantity;
    private ProductResponse product;

    public static CartResponse build(Cart cart) {

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setProductId(cart.getProduct().getId());
        response.setProductName(cart.getProduct().getName());
        response.setOptionId(cart.getProductOption().getId());
        response.setOptionName(cart.getProductOption().getName());
        response.setPrice(cart.getProductOption().getPrice());
        response.setQuantity(cart.getQuantity());
        response.setMaxQuantity(cart.getProductOption().getQuantity());

        return response;
    }

    public static CartResponse build(Cart cart, ProductResponse product) {

        CartResponse response = build(cart);
        response.setProduct(product);

        return response;
    }
}
