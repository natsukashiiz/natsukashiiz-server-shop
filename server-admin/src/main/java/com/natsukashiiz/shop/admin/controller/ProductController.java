package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.ProductDTO;
import com.natsukashiiz.shop.admin.service.ProductService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> queryAllProducts(ProductDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(productService.queryAllProduct(request, pagination));
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
