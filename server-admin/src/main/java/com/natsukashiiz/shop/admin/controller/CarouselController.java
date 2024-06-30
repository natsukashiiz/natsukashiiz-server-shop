package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Carousel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/carousels")
@AllArgsConstructor
public class CarouselController {

    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryCarousels() {
        return ResponseEntity.ok("Query all carousels");
    }

    @GetMapping("/{carouselId}")
    public ResponseEntity<?> queryCarouselById(@PathVariable Long carouselId) {
        return ResponseEntity.ok(String.format("Query carousel by id: %d", carouselId));
    }

    @PostMapping
    public ResponseEntity<?> createCarousel(@RequestBody Carousel carousel) {
        return ResponseEntity.ok(String.format("Create carousel: %s", carousel));
    }

    @PutMapping("/{carouselId}")
    public ResponseEntity<?> updateCarousel(@PathVariable Long carouselId, @RequestBody Carousel carousel) {
        return ResponseEntity.ok(String.format("Update carousel by id: %d, %s", carouselId, carousel));
    }

    @DeleteMapping("/{carouselId}")
    public ResponseEntity<?> deleteCarousel(@PathVariable Long carouselId) {
        return ResponseEntity.ok(String.format("Delete carousel by id: %d", carouselId));
    }
}
