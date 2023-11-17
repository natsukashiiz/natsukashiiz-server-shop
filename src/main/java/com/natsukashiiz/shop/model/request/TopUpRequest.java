package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class TopUpRequest {

    private String source;
    private Double amount;
}
