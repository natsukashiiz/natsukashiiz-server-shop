package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.ProductOption;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.ProductOption}
 */
@Getter
@Setter
@ToString
public class ProductOptionDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private Double price;
    private Integer quantity;
    private Integer sort;
    private ProductImageDTO image;

    public static ProductOptionDTO fromEntity(ProductOption option) {
        ProductOptionDTO response = new ProductOptionDTO();
        response.setId(option.getId());
        response.setCreatedAt(option.getCreatedAt());
        response.setUpdatedAt(option.getUpdatedAt());
        response.setName(option.getName());
        response.setPrice(option.getPrice());
        response.setQuantity(option.getQuantity());
        response.setSort(option.getSort());
        if (option.getImage() != null) {
            response.setImage(ProductImageDTO.fromEntity(option.getImage()));
        }
        return response;
    }

    public ProductOption toEntity() {
        ProductOption option = new ProductOption();
        option.setId(id);
        option.setName(name);
        option.setPrice(price);
        option.setQuantity(quantity);
        option.setSort(sort);
        return option;
    }
}