package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.CarouselDTO;
import com.natsukashiiz.shop.admin.service.CarouselService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/carousels")
@AllArgsConstructor
public class CarouselController {

    private final CarouselService carouselService;

    @GetMapping
    public ResponseEntity<?> queryAllCarousels(CarouselDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(carouselService.queryAllCarousels(request, pagination));
    }

    @GetMapping("/{carouselId}")
    public ResponseEntity<?> queryCarouselById(@PathVariable Long carouselId) throws BaseException {
        return ResponseEntity.ok(carouselService.queryCarouselById(carouselId));
    }

    @PostMapping
    public ResponseEntity<?> createCarousel(@RequestBody CarouselDTO request) throws BaseException {
        return ResponseEntity.ok(carouselService.createCarousel(request));
    }

    @PutMapping("/{carouselId}")
    public ResponseEntity<?> updateCarouselById(@PathVariable Long carouselId, @RequestBody CarouselDTO request) throws BaseException {
        return ResponseEntity.ok(carouselService.updateCarouselById(carouselId, request));
    }

    @DeleteMapping("/{carouselId}")
    public ResponseEntity<?> deleteCarouselById(@PathVariable Long carouselId) throws BaseException {
        carouselService.deleteCarouselById(carouselId);
        return ResponseEntity.ok().build();
    }
}
