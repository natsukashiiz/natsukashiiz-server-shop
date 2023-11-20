package com.natsukashiiz.shop.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PayOrderResponse {

    private UUID orderId;
    private String url;

    public static PayOrderResponse build(UUID orderId, String url) {

        PayOrderResponse response = new PayOrderResponse();
        response.setOrderId(orderId);
        response.setUrl(url);

        return response;
    }
}
