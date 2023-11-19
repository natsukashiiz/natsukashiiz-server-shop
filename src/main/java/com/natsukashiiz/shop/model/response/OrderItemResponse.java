package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private Long optionId;
    private String optionName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;

    public static OrderItemResponse build(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProductId())
                .productName(item.getProductName())
                .optionId(item.getOptionId())
                .optionName(item.getOptionName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
