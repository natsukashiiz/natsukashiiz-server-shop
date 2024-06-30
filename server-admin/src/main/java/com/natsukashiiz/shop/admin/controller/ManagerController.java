package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Admin;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/managers")
@AllArgsConstructor
public class ManagerController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryManagers() {
        return ResponseEntity.ok("Query all managers");
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<?> queryManagerById(@PathVariable Long managerId) {
        return ResponseEntity.ok(String.format("Query manager by id: %d", managerId));
    }

    @PostMapping
    public ResponseEntity<?> createManager(@RequestBody Admin manager) {
        return ResponseEntity.ok(String.format("Create manager: %s", manager));
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<?> updateManager(@PathVariable Long managerId, @RequestBody Admin manager) {
        return ResponseEntity.ok(String.format("Update manager by id: %d, %s", managerId, manager));
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<?> deleteManager(@PathVariable Long managerId) {
        return ResponseEntity.ok(String.format("Delete manager by id: %d", managerId));
    }
}
