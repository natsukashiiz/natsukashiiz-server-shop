package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) throws BaseException {
        return ResponseEntity.ok(categoryService.queryCategoryById(id));
    }
}
