package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.GoogleLoginRequest;
import com.natsukashiiz.shop.model.request.LoginRequest;
import com.natsukashiiz.shop.model.request.SignUpRequest;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.GoogleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;
    private final GoogleService googleService;

    @Operation(summary = "Login", description = "Login for get Token use apis")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws BaseException {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "SignUp", description = "SignUp for use in system and get Token use api")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) throws BaseException {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @Operation(summary = "Google", description = "SignUp / Login with google account")
    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody GoogleLoginRequest request) throws BaseException {
        return ResponseEntity.ok(googleService.login(request));
    }
}
