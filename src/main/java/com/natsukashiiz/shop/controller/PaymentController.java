package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.OrderBusiness;
import com.natsukashiiz.shop.payment.model.EventData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payment")
@AllArgsConstructor
@Log4j2
public class PaymentController {

    private final OrderBusiness orderBusiness;

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody EventData request) {
        orderBusiness.updateOrderFromWebhook(request);
        return ResponseEntity.ok(request);
    }
}
