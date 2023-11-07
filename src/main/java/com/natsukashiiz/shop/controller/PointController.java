package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.PointBusiness;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/point")
@AllArgsConstructor
public class PointController {
    private final PointBusiness pointBusiness;

    @GetMapping
    public ResponseEntity<?> myPoint() {
        return ResponseEntity.ok(pointBusiness.myPoint());
    }
}
