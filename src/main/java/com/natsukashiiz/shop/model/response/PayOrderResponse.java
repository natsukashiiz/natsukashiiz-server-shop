package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class PayOrderResponse {

    private UUID orderId;
    private String url;
}
