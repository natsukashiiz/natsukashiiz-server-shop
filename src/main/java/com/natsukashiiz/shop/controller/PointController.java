package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.PointBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "My Point", description = "My point")
    @GetMapping
    public ResponseEntity<?> myPoint() throws BaseException {
        return ResponseEntity.ok(pointBusiness.myPoint());
    }
}
