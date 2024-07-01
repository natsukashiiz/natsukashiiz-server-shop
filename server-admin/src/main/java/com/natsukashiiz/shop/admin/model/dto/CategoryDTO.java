package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Category}
 */
@Getter
@Setter
@ToString
public class CategoryDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private String thumbnail;
    private Integer sort;

    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO response = new CategoryDTO();
        response.setId(category.getId());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        response.setName(category.getName());
        response.setThumbnail(category.getThumbnail());
        response.setSort(category.getSort());
        return response;
    }

    public Category toEntity(){
        Category category = new Category();
        category.setId(this.id);
        category.setName(this.name);
        category.setThumbnail(this.thumbnail);
        category.setSort(this.sort);
        return category;
    }
}