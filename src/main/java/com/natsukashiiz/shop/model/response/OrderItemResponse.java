package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Long optionId;
    private String optionName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;

    public static OrderItemResponse build(OrderItem item) {

        OrderItemResponse response = new OrderItemResponse();
        response.setProductId(item.getProductId());
        response.setProductName(item.getProductName());
        response.setOptionId(item.getOptionId());
        response.setOptionName(item.getOptionName());
        response.setPrice(item.getPrice());
        response.setQuantity(item.getQuantity());
        response.setTotalPrice(item.getTotalPrice());

        return response;
    }
}
