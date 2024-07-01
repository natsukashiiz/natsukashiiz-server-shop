package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.UserDTO;
import com.natsukashiiz.shop.admin.service.UserService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> queryAllUsers(UserDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(userService.queryAllUsers(request, pagination));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> queryUserById(@PathVariable Long userId) throws BaseException {
        return ResponseEntity.ok(userService.queryUserById(userId));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO request) throws BaseException {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable Long userId, @RequestBody UserDTO request) throws BaseException {
        return ResponseEntity.ok(userService.updateUserById(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) throws BaseException {
        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }
}
