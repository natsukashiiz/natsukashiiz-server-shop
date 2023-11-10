package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account")
@AllArgsConstructor
@Tag(name = "Account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/code")
    public ResponseEntity<?> getNewVerifyCode() throws BaseException {
        accountService.getNewVerifyCode();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code) throws BaseException {
        accountService.verify(code);
        return ResponseEntity.ok().build();
    }
}
