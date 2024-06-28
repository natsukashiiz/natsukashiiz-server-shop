package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CreateOrderRequest {

    private Long voucherId;
    private List<OrderItem> orderItems;

    @Getter
    @Setter
    @ToString
    public static class OrderItem {
        private Long productId;
        private Long optionId;
        private Integer quantity;
    }
}
