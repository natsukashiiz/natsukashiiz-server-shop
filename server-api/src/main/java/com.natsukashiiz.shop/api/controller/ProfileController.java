package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.api.model.request.UpdateProfileRequest;
import com.natsukashiiz.shop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService accountService;

    @Operation(summary = "Get Profile", description = "Get my profile")
    @GetMapping
    public ResponseEntity<?> myProfile() throws BaseException {
        return ResponseEntity.ok(accountService.queryProfile());
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) throws BaseException {
        return ResponseEntity.ok(accountService.updateProfile(request));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteAvatar() throws BaseException {
        return ResponseEntity.ok(accountService.deleteAvatar());
    }

    @Operation(summary = "Change Password", description = "Change password")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        accountService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-history")
    public ResponseEntity<?> queryLoginHistory(PaginationRequest request) throws BaseException {
        return ResponseEntity.ok(accountService.queryLoginHistory(request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() throws BaseException {
        accountService.deleteUser();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get verify code", description = "Send verify code to email")
    @PostMapping("/code")
    public ResponseEntity<?> getVerifyCode() throws BaseException {
        accountService.sendVerifyCode();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Verify account", description = "Verify account with code")
    @PostMapping("/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code, HttpServletRequest httpServletRequest) throws BaseException {
        return ResponseEntity.ok(accountService.verifyCode(code, httpServletRequest));
    }
}
