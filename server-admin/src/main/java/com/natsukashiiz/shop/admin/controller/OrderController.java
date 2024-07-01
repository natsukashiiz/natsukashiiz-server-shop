package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.OrderDTO;
import com.natsukashiiz.shop.admin.service.OrderService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> queryAllOrders(OrderDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(orderService.queryAllOrders(request, pagination));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> queryOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(String.format("Query order by id: %d", orderId));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(String.format("Create order: %s", order));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        return ResponseEntity.ok(String.format("Update order by id: %d, %s", orderId, order));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(String.format("Delete order by id: %d", orderId));
    }
}
