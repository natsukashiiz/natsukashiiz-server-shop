package com.natsukashiiz.shop.api.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TopUpRequest {

    private String source;
    private Double amount;
}
