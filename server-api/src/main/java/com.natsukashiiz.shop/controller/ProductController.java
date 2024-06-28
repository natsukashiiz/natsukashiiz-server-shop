package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.ProductBusiness;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.ProductReviewRequest;
import com.natsukashiiz.shop.model.request.QueryProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductBusiness productBusiness;

    @Operation(summary = "Get All", description = "Get all product")
    @GetMapping("/all")
    private ResponseEntity<?> getAll() {
        return ResponseEntity.ok(productBusiness.getAll());
    }

    @GetMapping
    private ResponseEntity<?> queryList(QueryProductRequest request, PaginationRequest pagination) {
        return ResponseEntity.ok(productBusiness.queryList(request, pagination));
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

    @GetMapping("/{productId}/reviews")
    private ResponseEntity<?> queryReview(@PathVariable Long productId, PaginationRequest pagination) {
        return ResponseEntity.ok(productBusiness.queryReviews(productId, pagination));
    }

    @PostMapping("/{productId}/reviews")
    private ResponseEntity<?> createReview(@PathVariable Long productId, @RequestBody ProductReviewRequest request) throws BaseException {
        productBusiness.createReview(productId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view-history")
    private ResponseEntity<?> getViewHistory(PaginationRequest pagination) throws BaseException {
        return ResponseEntity.ok(productBusiness.queryViewHistory(pagination));
    }

    @GetMapping("/favorites")
    private ResponseEntity<?> getFavorite(PaginationRequest pagination) throws BaseException {
        return ResponseEntity.ok(productBusiness.queryFavorite(pagination));
    }

    @GetMapping("/{productId}/favorites")
    private ResponseEntity<?> isFavorite(@PathVariable Long productId) throws BaseException {
        return ResponseEntity.ok(productBusiness.isFavorite(productId));
    }

    @PostMapping("/{productId}/favorites")
    private ResponseEntity<?> favorite(@PathVariable Long productId) throws BaseException {
        return ResponseEntity.ok(productBusiness.favorite(productId));
    }
}
