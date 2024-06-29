package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.api.model.response.CarouselResponse;
import com.natsukashiiz.shop.api.service.CarouselService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/carousels")
@AllArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    @GetMapping
    public ResponseEntity<?> getCarousels() {
        List<CarouselResponse> carousels = carouselService.queryAllCarousel();
        return ResponseEntity.ok(carousels);
    }
}
