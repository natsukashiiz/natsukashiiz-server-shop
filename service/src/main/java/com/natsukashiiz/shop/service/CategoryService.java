package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Category;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CategoryException;
import com.natsukashiiz.shop.model.response.CategoryResponse;
import com.natsukashiiz.shop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryResponse.buildList(categories);
    }

    public CategoryResponse queryCategoryById(Long categoryId) throws BaseException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            log.warn("QueryCategoryById-[block]:(category not found). categoryId:{}", categoryId);
            throw CategoryException.invalid();
        }

        Category category = categoryOptional.get();
        return CategoryResponse.build(category);
    }
}
