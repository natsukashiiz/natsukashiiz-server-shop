package com.natsukashiiz.shop.admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@AllArgsConstructor
public class PaymentController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryPayments() {
        return ResponseEntity.ok("Query all payments");
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> queryPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(String.format("Query payment by id: %d", paymentId));
    }

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Object payment) {
        return ResponseEntity.ok(String.format("Create payment: %s", payment));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<?> updatePayment(@PathVariable Long paymentId, @RequestBody Object payment) {
        return ResponseEntity.ok(String.format("Update payment by id: %d, %s", paymentId, payment));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(String.format("Delete payment by id: %d", paymentId));
    }
}
