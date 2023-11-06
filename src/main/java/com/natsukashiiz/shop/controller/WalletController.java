package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.service.WalletService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wallet")
@AllArgsConstructor
@Log4j2
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> get(Authentication authentication) {
        return walletService.get(authentication);
    }

    @PostMapping
    public ResponseEntity<WalletResponse> create(Authentication authentication) {
        return walletService.create(authentication);
    }
}
