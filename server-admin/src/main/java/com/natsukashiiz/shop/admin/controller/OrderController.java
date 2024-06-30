package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryOrders() {
        return ResponseEntity.ok("Query all orders");
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
