package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private List<ProductOptionResponse> options;

    public static ProductResponse build(Product product, List<ProductOptionResponse> options) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .options(options)
                .build();
    }

    public static ProductResponse build(Product product) {
        List<ProductOptionResponse> productOptionResponses = product.getOptions()
                .stream()
                .map(ProductOptionResponse::build)
                .collect(Collectors.toList());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .options(productOptionResponses)
                .build();
    }
}
