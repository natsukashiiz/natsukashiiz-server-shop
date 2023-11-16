package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.CartBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CartRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@AllArgsConstructor
public class CartController {

    private final CartBusiness cartBusiness;

    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(cartBusiness.getAll());
    }

    @PutMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody CartRequest request) throws BaseException {
        return ResponseEntity.ok(cartBusiness.upsert(request));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> delete(@PathVariable Long cartId) throws BaseException {
        cartBusiness.delete(cartId);
        return ResponseEntity.ok().build();
    }
}
