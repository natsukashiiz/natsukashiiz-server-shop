package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Product}
 */
@Getter
@Setter
@ToString
public class ProductDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private CategoryDTO category;
    private List<ProductOptionDTO> options;
    private List<ProductImageDTO> images;
    private String description;
    private Long views;
    private Long orders;
    private Float rating;
    private Long reviews;

    public static ProductDTO fromEntity(Product product) {
        ProductDTO response = new ProductDTO();
        response.setId(product.getId());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setViews(product.getViews());
        response.setOrders(product.getOrders());
        response.setRating(product.getRating());
        response.setReviews(product.getReviews());

        response.setCategory(CategoryDTO.fromEntity(product.getCategory()));
        response.setOptions(product.getOptions().stream().map(ProductOptionDTO::fromEntity).collect(Collectors.toList()));
        response.setImages(product.getImages().stream().map(ProductImageDTO::fromEntity).collect(Collectors.toList()));

        return response;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setViews(views);
        product.setOrders(orders);
        product.setRating(rating);
        product.setReviews(reviews);

        if (category != null) {
            product.setCategory(category.toEntity());
        }

        if (options != null) {
            product.setOptions(options.stream().map(ProductOptionDTO::toEntity).collect(Collectors.toList()));
        }

        if (images != null) {
            product.setImages(images.stream().map(ProductImageDTO::toEntity).collect(Collectors.toList()));
        }

        return product;
    }
}