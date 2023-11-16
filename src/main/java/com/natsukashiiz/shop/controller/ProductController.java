package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.ProductBusiness;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final ProductBusiness productBusiness;

    @Operation(summary = "Get List", description = "Products list")
    @GetMapping
    private ResponseEntity<?> getList() {
        return ResponseEntity.ok(productBusiness.getAll());
    }
}
