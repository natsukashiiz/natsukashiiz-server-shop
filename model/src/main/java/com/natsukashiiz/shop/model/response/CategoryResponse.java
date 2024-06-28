package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String thumbnail;

    public static CategoryResponse build(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setThumbnail(category.getThumbnail());
        return response;
    }

    public static List<CategoryResponse> buildList(List<Category> categories) {
        return categories.stream()
                .sorted(Comparator.comparingInt(Category::getSort))
                .map(CategoryResponse::build)
                .collect(Collectors.toList());
    }
}
