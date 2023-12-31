package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.ProductBusiness;
import com.natsukashiiz.shop.common.Pagination;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductBusiness productBusiness;

    @Operation(summary = "Get All", description = "Get all product")
    @GetMapping
    private ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productBusiness.getAll());
    }

    @Operation(summary = "Get All With Pagination", description = "Get all product With Pagination")
    @GetMapping("/p")
    private ResponseEntity<?> getAllWithPagination(PaginationRequest pagination) {
        return ResponseEntity.ok(productBusiness.getPage(pagination));
    }

    @Operation(summary = "Get Top Seller", description = "Get Top Seller")
    @GetMapping("/top")
    private ResponseEntity<?> getTopSeller() {
        return ResponseEntity.ok("Top Seller");
    }

    @Operation(summary = "Get One", description = "Get product by id")
    @GetMapping("/{productId}")
    private ResponseEntity<?> getById(@PathVariable Long productId) throws BaseException {
        return ResponseEntity.ok(productBusiness.getById(productId));
    }
}
