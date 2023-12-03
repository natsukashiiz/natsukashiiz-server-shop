package com.natsukashiiz.shop.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.natsukashiiz.shop.entity.ProductOption;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductOptionResponse implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private Boolean selected;

    public static ProductOptionResponse build(ProductOption option) {

        ProductOptionResponse response = new ProductOptionResponse();
        response.setId(option.getId());
        response.setName(option.getName());
        response.setPrice(option.getPrice());
        response.setQuantity(option.getQuantity());

        return response;
    }
}
