package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.OrderBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
@Tag(name = "Orders")
@Log4j2
public class OrderController {
    private final OrderBusiness orderBusiness;

    @Operation(summary = "My Orders", description = "My orders list")
    @GetMapping
    public ResponseEntity<?> myOrders() throws BaseException {
        return ResponseEntity.ok(orderBusiness.myOrders());
    }
}
