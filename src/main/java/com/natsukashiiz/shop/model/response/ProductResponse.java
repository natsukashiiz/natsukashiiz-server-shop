package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
