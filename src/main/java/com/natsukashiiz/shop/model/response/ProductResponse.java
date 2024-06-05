package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String thumbnail;
    private List<ProductOptionResponse> options;
    private Long views;
    private Long orders;
    private LocalDateTime createdAt;

    public static ProductResponse build(Product product, List<ProductOptionResponse> options) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .thumbnail(product.getThumbnail())
                .options(options)
                .views(product.getViews())
                .orders(product.getOrders())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static ProductResponse build(Product product) {
        List<ProductOptionResponse> productOptionResponses = product.getOptions()
                .stream()
                .map(ProductOptionResponse::build)
                .collect(Collectors.toList());

        return ProductResponse.build(product, productOptionResponses);
    }
}
