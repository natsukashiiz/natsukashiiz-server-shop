package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.ProductImage}
 */
@Getter
@Setter
@ToString
public class ProductImageDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String url;
    private Integer sort;

    public static ProductImageDTO fromEntity(ProductImage productImage) {
        ProductImageDTO response = new ProductImageDTO();
        response.setId(productImage.getId());
        response.setCreatedAt(productImage.getCreatedAt());
        response.setUpdatedAt(productImage.getUpdatedAt());
        response.setUrl(productImage.getUrl());
        response.setSort(productImage.getSort());
        return response;
    }

    public ProductImage toEntity() {
        ProductImage productImage = new ProductImage();
        productImage.setId(this.id);
        productImage.setCreatedAt(this.createdAt);
        productImage.setUpdatedAt(this.updatedAt);
        productImage.setUrl(this.url);
        productImage.setSort(this.sort);
        return productImage;
    }
}