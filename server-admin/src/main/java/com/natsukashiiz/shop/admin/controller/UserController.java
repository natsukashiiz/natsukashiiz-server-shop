package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryUsers() {
        return ResponseEntity.ok("Query all users");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> queryUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(String.format("Query user by id: %d", userId));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return ResponseEntity.ok(String.format("Create user: %s", user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
        return ResponseEntity.ok(String.format("Update user by id: %d, %s", userId, user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(String.format("Delete user by id: %d", userId));
    }
}
