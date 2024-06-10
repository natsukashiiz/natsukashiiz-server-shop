package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.CarouselBusiness;
import com.natsukashiiz.shop.model.response.CarouselResponse;
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

    private final CarouselBusiness carouselBusiness;

    @GetMapping
    public ResponseEntity<?> getCarousels() {
        List<CarouselResponse> carousels = carouselBusiness.getCarousels();
        return ResponseEntity.ok(carousels);
    }
}
