package com.natsukashiiz.shop.api.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartRequest {
    private Long productId;
    private Long optionId;
    private Integer quantity;
    private Boolean selected;
}
