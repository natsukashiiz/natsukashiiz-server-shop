package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.OrderBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
@Tag(name = "Orders")
@Log4j2
public class OrderController {
    private final OrderBusiness orderBusiness;

    @GetMapping("/{orderId}")
    public ResponseEntity<?> myOrder(@PathVariable String orderId) throws BaseException {
        return ResponseEntity.ok(orderBusiness.myOrderById(orderId));
    }

    @Operation(summary = "My Orders", description = "My orders list")
    @GetMapping
    public ResponseEntity<?> myOrders() throws BaseException {
        return ResponseEntity.ok(orderBusiness.myOrders());
    }

    @Operation(summary = "Create Orders", description = "Create Orders")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody List<CreateOrderRequest> request) throws BaseException {
        return ResponseEntity.ok(orderBusiness.create(request));
    }

    @PutMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestBody PayOrderRequest request) throws BaseException {
        return ResponseEntity.ok(orderBusiness.pay(request));
    }
}
