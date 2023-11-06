package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    private ResponseEntity<List<ProductResponse>> getList() {
        return productService.getList();
    }

    @PostMapping("/buy")
    private ResponseEntity<OrderResponse> buy(@Valid @RequestBody BuyRequest req, Authentication authentication) {
        return orderService.buy(req, authentication);
    }
}
