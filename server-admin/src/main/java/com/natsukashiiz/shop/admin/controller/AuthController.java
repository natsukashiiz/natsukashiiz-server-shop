package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.request.LoginRequest;
import com.natsukashiiz.shop.admin.model.request.SignUpRequest;
import com.natsukashiiz.shop.admin.service.AuthService;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login", description = "Login for get Token use apis")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.login(request, httpServletRequest));
    }

    @Operation(summary = "SignUp", description = "SignUp for use in system and get Token use api")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.signUp(request, httpServletRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.refresh(request, httpServletRequest));
    }

//    @Operation(summary = "Forgot Password", description = "Forgot password")
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) throws BaseException {
//        accountService.forgotPassword(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @Operation(summary = "Reset Password", description = "Reset password")
//    @PatchMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {
//        return ResponseEntity.ok(accountService.resetPassword(request, httpServletRequest));
//    }
}