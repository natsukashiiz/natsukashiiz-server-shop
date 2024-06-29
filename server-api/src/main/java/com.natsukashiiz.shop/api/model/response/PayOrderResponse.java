package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.common.PayUrlType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PayOrderResponse {

    private UUID orderId;
    private PayUrlType type;
    private String url;

    public static PayOrderResponse build(UUID orderId, PayUrlType type, String url) {

        PayOrderResponse response = new PayOrderResponse();
        response.setOrderId(orderId);
        response.setType(type);
        response.setUrl(url);

        return response;
    }
}
