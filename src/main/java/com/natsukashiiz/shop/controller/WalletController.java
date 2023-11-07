package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.WalletBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wallet")
@AllArgsConstructor
public class WalletController {
    private final WalletBusiness walletBusiness;

    @GetMapping
    public ResponseEntity<?> myWallet() throws BaseException {
        return ResponseEntity.ok(walletBusiness.myWallet());
    }

    @PostMapping
    public ResponseEntity<?> create() throws BaseException {
        return ResponseEntity.ok(walletBusiness.create());
    }
}
