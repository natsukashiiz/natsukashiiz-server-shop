package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.admin.model.dto.VoucherDTO;
import com.natsukashiiz.shop.admin.service.VoucherService;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.exception.BaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vouchers")
@AllArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<?> queryAllCategories(VoucherDTO request, PaginationRequest pagination) {
        return ResponseEntity.ok(voucherService.queryAllCategories(request, pagination));
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<?> queryVoucherById(@PathVariable Long voucherId) throws BaseException {
        return ResponseEntity.ok(voucherService.queryVoucherById(voucherId));
    }

    @PostMapping
    public ResponseEntity<?> createVoucher(@RequestBody VoucherDTO request) throws BaseException {
        return ResponseEntity.ok(voucherService.createVoucher(request));
    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<?> updateVoucherById(@PathVariable Long voucherId, @RequestBody VoucherDTO request) throws BaseException {
        return ResponseEntity.ok(voucherService.updateVoucherById(voucherId, request));
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<?> deleteVoucherById(@PathVariable Long voucherId) throws BaseException {
        voucherService.deleteVoucherById(voucherId);
        return ResponseEntity.ok().build();
    }
}
