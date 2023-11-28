package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.AccountBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.ChangePasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/account")
@AllArgsConstructor
public class AccountController {
    private final AccountBusiness accountBusiness;

    @Operation(summary = "Get Profile", description = "Get my profile")
    @GetMapping("/profile")
    public ResponseEntity<?> myProfile() {
        return ResponseEntity.ok("my profile");
    }

    @Operation(summary = "Get verify code", description = "Send verify code to email")
    @PostMapping("/code")
    public ResponseEntity<?> getVerifyCode() throws BaseException {
        accountBusiness.getVerifyCode();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify account", description = "Verify account with code")
    @PostMapping("/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code) throws BaseException {
        return ResponseEntity.ok(accountBusiness.verify(code));
    }

    @Operation(summary = "Change Password", description = "Change password")
    @PatchMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) throws BaseException {
        accountBusiness.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset Password", description = "Reset password")
    @PatchMapping("/password/reset")
    public ResponseEntity<?> resetPassword() {
        return ResponseEntity.ok("reset password");
    }
}
