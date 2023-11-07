package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.ProductBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.BuyRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductBusiness productBusiness;

    @GetMapping
    private ResponseEntity<?> getList() {
        return ResponseEntity.ok(productBusiness.getAll());
    }

    @PostMapping("/buy")
    private ResponseEntity<?> buy(@RequestBody List<BuyRequest> request) throws BaseException {
        return ResponseEntity.ok(productBusiness.buy(request));
    }
}
