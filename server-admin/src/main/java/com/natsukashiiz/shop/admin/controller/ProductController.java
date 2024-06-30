package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryProducts() {
        return ResponseEntity.ok("Query all products");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> queryProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(String.format("Query product by id: %d", productId));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(String.format("Create product: %s", product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return ResponseEntity.ok(String.format("Update product by id: %d, %s", productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(String.format("Delete product by id: %d", productId));
    }
}
