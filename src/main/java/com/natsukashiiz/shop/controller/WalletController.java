package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.WalletBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.TopUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wallet")
@AllArgsConstructor
@Tag(name = "Wallet")
public class WalletController {
    private final WalletBusiness walletBusiness;

    @Operation(summary = "My Wallet", description = "My wallet")
    @GetMapping
    public ResponseEntity<?> myWallet() throws BaseException {
        return ResponseEntity.ok(walletBusiness.myWallet());
    }

    @Operation(summary = "Create Wallet", description = "Create new wallet, But limit 1.")
    @PostMapping
    public ResponseEntity<?> create() throws BaseException {
        return ResponseEntity.ok(walletBusiness.create());
    }

    @PutMapping
    public ResponseEntity<?> topUp(@RequestBody TopUpRequest request) throws BaseException {
        return ResponseEntity.ok(walletBusiness.topUp(request));
    }
}
