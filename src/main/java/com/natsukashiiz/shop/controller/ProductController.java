package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.common.ApiResponse;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    private ResponseEntity<?> getList() {
        return productService.getList();
    }

    @PostMapping("/buy")
    private ApiResponse<?> buy(@RequestBody List<BuyRequest> req, Authentication authentication) {
        return orderService.buy(req, authentication);
    }
}
