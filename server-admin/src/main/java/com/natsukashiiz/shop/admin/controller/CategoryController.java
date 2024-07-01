package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.CategoryDTO;
import com.natsukashiiz.shop.admin.service.CategoryService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> queryAllCategories(CategoryDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(categoryService.queryAllCategories(request, pagination));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> queryCategoryById(@PathVariable Long categoryId) throws BaseException {
        return ResponseEntity.ok(categoryService.queryCategoryById(categoryId));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO request) throws BaseException {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategoryById(@PathVariable Long categoryId, @RequestBody CategoryDTO request) throws BaseException {
        return ResponseEntity.ok(categoryService.updateCategoryById(categoryId, request));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long categoryId) throws BaseException {
        categoryService.deleteCategoryById(categoryId);
        return ResponseEntity.ok().build();
    }
}
