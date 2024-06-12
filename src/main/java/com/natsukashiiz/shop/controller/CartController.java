package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.CartBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CartRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/cart")
@AllArgsConstructor
public class CartController {

    private final CartBusiness cartBusiness;

    @Operation(summary = "Get All", description = "Get all cart")
    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(cartBusiness.getAll());
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() throws BaseException {
        return ResponseEntity.ok(cartBusiness.count());
    }

    @Operation(summary = "Create or Update", description = "Create or Update cart")
    @PutMapping
    public ResponseEntity<?> createOrUpdate(@RequestBody CartRequest request) throws BaseException {
        return ResponseEntity.ok(cartBusiness.upsert(request));
    }

    @Operation(summary = "Delete", description = "Delete cart by cart id")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> delete(@PathVariable Long cartId) throws BaseException {
        cartBusiness.delete(cartId);
        return ResponseEntity.ok().build();
    }
}
