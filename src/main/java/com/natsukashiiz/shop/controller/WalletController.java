package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.service.WalletService;
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
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> get() throws BaseException {
        return walletService.get();
    }

    @PostMapping
    public ResponseEntity<WalletResponse> create() throws BaseException {
        return walletService.create();
    }
}
