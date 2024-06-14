package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductImage;
import com.natsukashiiz.shop.entity.ProductOption;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;
    private List<ProductOptionResponse> options;
    private List<String> images;
    private CategoryResponse category;
    private Long views;
    private Long orders;
    private Long reviews;
    private Float rating;
    private LocalDateTime createdAt;

    public static List<ProductResponse> buildList(List<Product> products) {
        return products.stream()
                .map(ProductResponse::build)
                .collect(Collectors.toList());
    }

    public static ProductResponse build(Product product, List<ProductOptionResponse> options, List<String> images) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .thumbnail(!images.isEmpty() ? images.get(0) : null)
                .options(options)
                .images(images)
                .category(CategoryResponse.build(product.getCategory()))
                .views(product.getViews())
                .orders(product.getOrders())
                .reviews(product.getReviews())
                .rating(product.getRating())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public static ProductResponse build(Product product) {
        List<ProductOptionResponse> productOptionResponses = product.getOptions()
                .stream()
                .sorted(Comparator.comparingInt(ProductOption::getSort))
                .map(ProductOptionResponse::build)
                .collect(Collectors.toList());

        List<String> productImageResponses = product.getImages()
                .stream()
                .sorted(Comparator.comparingInt(ProductImage::getSort))
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());

        return ProductResponse.build(product, productOptionResponses, productImageResponses);
    }
}
