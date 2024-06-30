package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
@AllArgsConstructor
public class CategoryController {

    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryCategories() {
        return ResponseEntity.ok("Query all categories");
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> queryCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(String.format("Query category by id: %d", categoryId));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(String.format("Create category: %s", category));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        return ResponseEntity.ok(String.format("Update category by id: %d, %s", categoryId, category));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(String.format("Delete category by id: %d", categoryId));
    }
}
