package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.OrderBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CheckoutRequest;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderBusiness orderBusiness;

    @Operation(summary = "Get One", description = "Get order by order id")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> myOrder(@PathVariable String orderId) throws BaseException {
        return ResponseEntity.ok(orderBusiness.myOrderById(orderId));
    }

    @Operation(summary = "Get All", description = "Get My all order")
    @GetMapping
    public ResponseEntity<?> myOrders(@RequestParam("status") String status) throws BaseException {
        return ResponseEntity.ok(orderBusiness.myOrders(status));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) throws BaseException {
        return ResponseEntity.ok(orderBusiness.checkout(request));
    }

    @Operation(summary = "Create", description = "Create order")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) throws BaseException {
        return ResponseEntity.ok(orderBusiness.create(request));
    }

    @Operation(summary = "Pay", description = "Pay order")
    @PutMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestBody PayOrderRequest request) throws BaseException {
        return ResponseEntity.ok(orderBusiness.pay(request));
    }

    @Operation(summary = "Cancel", description = "Cancel order")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) throws BaseException {
        return ResponseEntity.ok(orderBusiness.cancel(orderId));
    }
}
