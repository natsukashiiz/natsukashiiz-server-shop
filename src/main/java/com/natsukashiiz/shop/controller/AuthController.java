package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.AccountBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.*;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.GoogleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final GoogleService googleService;
    private final AccountBusiness accountBusiness;

    @Operation(summary = "Login", description = "Login for get Token use apis")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.login(request, httpServletRequest));
    }

    @Operation(summary = "SignUp", description = "SignUp for use in system and get Token use api")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.signUp(request,httpServletRequest));
    }

    @Operation(summary = "Google", description = "SignUp / Login with google account")
    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody GoogleLoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(googleService.login(request,httpServletRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(authService.refresh(request, httpServletRequest));
    }

    @Operation(summary = "Get verify code", description = "Send verify code to email")
    @PostMapping("/code")
    public ResponseEntity<?> getVerifyCode() throws BaseException {
        accountBusiness.getVerifyCode();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify account", description = "Verify account with code")
    @PostMapping("/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(accountBusiness.verify(code, httpServletRequest));
    }

    @Operation(summary = "Forgot Password", description = "Forgot password")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) throws BaseException {
        accountBusiness.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset Password", description = "Reset password")
    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(accountBusiness.resetPassword(request, httpServletRequest));
    }
}
