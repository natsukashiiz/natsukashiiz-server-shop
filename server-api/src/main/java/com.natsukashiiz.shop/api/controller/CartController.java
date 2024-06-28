package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CartRequest;
import com.natsukashiiz.shop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get All", description = "Get all cart")
    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(cartService.queryAllCart());
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() throws BaseException {
        return ResponseEntity.ok(cartService.countCart());
    }

    @Operation(summary = "Create or Update", description = "Create or Update cart")
    @PutMapping
    public ResponseEntity<?> updateCart(@RequestBody List<CartRequest> requests) throws BaseException {
            return ResponseEntity.ok(cartService.updateCart(requests));
    }

    @Operation(summary = "Delete", description = "Delete cart by cart id")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> delete(@PathVariable Long cartId) throws BaseException {
        return ResponseEntity.ok(cartService.delete(cartId));
    }
}
