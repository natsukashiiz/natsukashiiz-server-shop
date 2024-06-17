package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.ProductFavorite;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.ProductFavorite}
 */
@Getter
@Setter
@ToString
public class ProductFavoriteResponse implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private ProductResponse product;

    public static ProductFavoriteResponse build(ProductFavorite productFavorite) {
        ProductFavoriteResponse response = new ProductFavoriteResponse();
        response.setId(productFavorite.getId());
        response.setCreatedAt(productFavorite.getCreatedAt());
        response.setProduct(ProductResponse.build(productFavorite.getProduct()));
        return response;
    }

    public static List<ProductFavoriteResponse> buildList(List<ProductFavorite> productFavorites) {
        return productFavorites.stream()
                .map(ProductFavoriteResponse::build)
                .collect(Collectors.toList());
    }
}