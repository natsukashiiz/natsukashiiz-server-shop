package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CheckoutRequest;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.request.PayOrderRequest;
import com.natsukashiiz.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get One", description = "Get order by order id")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> myOrder(@PathVariable String orderId) throws BaseException {
        return ResponseEntity.ok(orderService.queryOrderById(orderId));
    }

    @Operation(summary = "Get All", description = "Get My all order")
    @GetMapping
    public ResponseEntity<?> myOrders(@RequestParam("status") String status) throws BaseException {
        return ResponseEntity.ok(orderService.queryAllOrderByStatus(status));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) throws BaseException {
        return ResponseEntity.ok(orderService.checkout(request));
    }

    @Operation(summary = "Create", description = "Create order")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) throws BaseException {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @Operation(summary = "Pay", description = "Pay order")
    @PutMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestBody PayOrderRequest request) throws BaseException {
        return ResponseEntity.ok(orderService.payOrder(request));
    }

    @Operation(summary = "Cancel", description = "Cancel order")
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) throws BaseException {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
