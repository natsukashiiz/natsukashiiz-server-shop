package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.LoginRequest;
import com.natsukashiiz.shop.model.request.SignUpRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws BaseException {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "SignUp")
    @PostMapping("/signUp")
    public ResponseEntity<TokenResponse> signUp(@RequestBody SignUpRequest request) throws BaseException {
        return ResponseEntity.ok(authService.signUp(request));
    }
}
