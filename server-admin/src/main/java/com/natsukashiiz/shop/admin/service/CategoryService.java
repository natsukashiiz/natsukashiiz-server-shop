package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.CategoryDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.Category;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CategoryException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    public PageResponse<List<CategoryDTO>> queryAllCategories(CategoryDTO request, PaginationRequest pagination) {
        Example<Category> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Category> page = categoryRepository.findAll(example, pagination);
        List<CategoryDTO> categorys = page.getContent()
                .stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(categorys, page.getTotalElements());
    }

    public CategoryDTO queryCategoryById(Long categoryId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            log.warn("QueryCategoryById-[block]:(category not found). categoryId:{}, adminId:{}", categoryId, admin.getId());
            throw CategoryException.invalid();
        }

        return CategoryDTO.fromEntity(categoryOptional.get());
    }

    public CategoryDTO createCategory(CategoryDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ObjectUtils.isEmpty(request.getName())) {
            log.warn("CreateCategory-[block]:(name is empty). adminId:{}", admin.getId());
            throw CategoryException.invalidName();
        }

        if (ObjectUtils.isEmpty(request.getThumbnail())) {
            log.warn("CreateCategory-[block]:(thumbnail is empty). adminId:{}", admin.getId());
            throw CategoryException.invalidThumbnail();
        }

        if (ObjectUtils.isEmpty(request.getSort())) {
            log.warn("CreateCategory-[block]:(sort is empty). adminId:{}", admin.getId());
            throw CategoryException.invalidSort();
        }

        Category category = request.toEntity();
        Category saved = categoryRepository.save(category);
        return CategoryDTO.fromEntity(saved);
    }

    public CategoryDTO updateCategoryById(Long categoryId, CategoryDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (!categoryOptional.isPresent()) {
            log.warn("UpdateCategoryById-[block]:(category not found). categoryId:{}, adminId:{}", categoryId, admin.getId());
            throw CategoryException.invalid();
        }

        Category category = categoryOptional.get();
        category.setName(request.getName());
        category.setThumbnail(request.getThumbnail());
        category.setSort(request.getSort());

        Category saved = categoryRepository.save(category);
        return CategoryDTO.fromEntity(saved);
    }

    public void deleteCategoryById(Long categoryId) throws BaseException {
        Admin admin = authService.getAdmin();

        boolean exists = categoryRepository.existsById(categoryId);
        if (!exists) {
            log.warn("DeleteCategoryById-[block]:(category not found). categoryId:{}, adminId:{}", categoryId, admin.getId());
            throw CategoryException.invalid();
        }

        categoryRepository.deleteById(categoryId);
    }
}
