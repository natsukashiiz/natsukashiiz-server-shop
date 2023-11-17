package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class PayOrderRequest {

    private String source;
    private String orderId;
}
