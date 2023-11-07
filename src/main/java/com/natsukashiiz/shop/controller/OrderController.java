package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.OrderBusiness;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderBusiness orderBusiness;

    @GetMapping
    public ResponseEntity<?> myOrders() {
        return ResponseEntity.ok(orderBusiness.myOrders());
    }
}
