package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    private ResponseEntity<?> getList() {
        return ResponseEntity.ok(productService.getList());
    }

    @PostMapping("/buy")
    private ResponseEntity<?> buy(@RequestBody List<BuyRequest> req) throws BaseException {
        return ResponseEntity.ok(productService.buy(req));
    }
}
