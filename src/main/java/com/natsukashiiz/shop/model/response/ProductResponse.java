package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
