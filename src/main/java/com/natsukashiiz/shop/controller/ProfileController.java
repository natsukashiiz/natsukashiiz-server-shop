package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.AccountBusiness;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.model.request.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/profile")
@AllArgsConstructor
public class ProfileController {

    private final AccountBusiness accountBusiness;

    @Operation(summary = "Get Profile", description = "Get my profile")
    @GetMapping
    public ResponseEntity<?> myProfile() throws BaseException {
        return ResponseEntity.ok(accountBusiness.queryProfile());
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) throws BaseException {
        return ResponseEntity.ok(accountBusiness.updateProfile(request));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteAvatar() throws BaseException {
        return ResponseEntity.ok(accountBusiness.deleteAvatar());
    }

    @Operation(summary = "Change Password", description = "Change password")
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        accountBusiness.changePassword(request, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-history")
    public ResponseEntity<?> queryLoginHistory(PaginationRequest request) throws BaseException {
        return ResponseEntity.ok(accountBusiness.queryLoginHistory(request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount() throws BaseException {
        accountBusiness.deleteAccount();
        return ResponseEntity.ok().build();
    }
}
