package com.natsukashiiz.shop.event;

import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderEvent {
    private final List<CreateOrderRequest> createOrderRequests;

    public CreateOrderEvent(List<CreateOrderRequest> createOrderRequests) {
        this.createOrderRequests = createOrderRequests;
    }
}
