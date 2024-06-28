package com.natsukashiiz.shop.api.controller;

import co.omise.models.Charge;
import co.omise.models.Event;
import com.natsukashiiz.shop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
@AllArgsConstructor
public class PaymentController {

    private final OrderService orderService;

    @Operation(summary = "Webhook", description = "Webhook for omise")
    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody Event<Charge> request) {
        orderService.updateOrderFromWebhook(request);
        return ResponseEntity.ok(request);
    }
}
