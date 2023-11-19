package com.natsukashiiz.shop.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.natsukashiiz.shop.entity.ProductOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOptionResponse {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private Boolean selected;

    public static ProductOptionResponse build(ProductOption option) {
        return ProductOptionResponse.builder()
                .id(option.getId())
                .name(option.getName())
                .price(option.getPrice())
                .quantity(option.getQuantity())
                .build();
    }
}
