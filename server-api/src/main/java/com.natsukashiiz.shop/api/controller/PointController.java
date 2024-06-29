package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/point")
@AllArgsConstructor
public class PointController {
    private final PointService pointService;

    @Operation(summary = "My Point", description = "My point")
    @GetMapping
    public ResponseEntity<?> myPoint() throws BaseException {
        return ResponseEntity.ok(pointService.queryPoint());
    }
}
