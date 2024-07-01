package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.ManagerDTO;
import com.natsukashiiz.shop.admin.service.ManagerService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/managers")
@AllArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ResponseEntity<?> queryAllManagers(ManagerDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(managerService.queryAllManagers(request, pagination));
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<?> queryManagerById(@PathVariable Long managerId) throws BaseException {
        return ResponseEntity.ok(managerService.queryManagerById(managerId));
    }

    @PostMapping
    public ResponseEntity<?> createManager(@RequestBody ManagerDTO request) throws BaseException {
        return ResponseEntity.ok(managerService.createManager(request));
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<?> updateManagerById(@PathVariable Long managerId, @RequestBody ManagerDTO request) throws BaseException {
        return ResponseEntity.ok(managerService.updateManagerById(managerId, request));
    }

    @DeleteMapping("/{managerId}")
    public ResponseEntity<?> deleteManagerById(@PathVariable Long managerId) throws BaseException {
        managerService.deleteManagerById(managerId);
        return ResponseEntity.ok().build();
    }
}
