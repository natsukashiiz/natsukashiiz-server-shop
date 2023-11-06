package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BuyResponse {
    private List<OrderResponse> orders;
    private int quantity;
    private double paid;
}
