package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.CategoryResponse;
import com.natsukashiiz.shop.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class CategoryBusiness {

    private final CategoryService categoryService;

    public List<CategoryResponse> getAllCategories() {
        return CategoryResponse.buildList(categoryService.getList());
    }

    public CategoryResponse getCategoryById(Long id) throws BaseException {
        return CategoryResponse.build(categoryService.getById(id));
    }
}
