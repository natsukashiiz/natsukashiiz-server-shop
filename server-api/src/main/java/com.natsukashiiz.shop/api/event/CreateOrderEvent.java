package com.natsukashiiz.shop.api.event;

import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderEvent {
    private final List<CreateOrderRequest.OrderItem> createOrderRequests;

    public CreateOrderEvent(List<CreateOrderRequest.OrderItem> createOrderRequests) {
        this.createOrderRequests = createOrderRequests;
    }
}
