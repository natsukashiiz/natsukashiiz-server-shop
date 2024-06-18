package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Cart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Long optionId;
    private String optionName;
    private Double price;
    private Integer quantity;
    private Integer maxQuantity;
    private ProductResponse product;
    private Boolean selected;


    public static CartItemResponse build(Cart cart) {

        CartItemResponse response = new CartItemResponse();
        response.setId(cart.getId());
        response.setProductId(cart.getProduct().getId());
        response.setProductName(cart.getProduct().getName());
        response.setOptionId(cart.getProductOption().getId());
        response.setOptionName(cart.getProductOption().getName());
        response.setPrice(cart.getProductOption().getPrice());
        response.setQuantity(cart.getQuantity());
        response.setMaxQuantity(cart.getProductOption().getQuantity());
        response.setProduct(ProductResponse.build(cart.getProduct()));
        response.setSelected(cart.getSelected());

        return response;
    }
}
