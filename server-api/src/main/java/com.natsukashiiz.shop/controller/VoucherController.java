package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.VoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vouchers")
@AllArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<?> queryVouchers() throws BaseException {
        return ResponseEntity.ok(voucherService.queryVouchers());
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<?> queryVoucherById(@PathVariable Long voucherId) throws BaseException {
        return ResponseEntity.ok(voucherService.queryVoucherById(voucherId));
    }

    @PostMapping("/{voucherId}/claim")
    public ResponseEntity<?> claimVoucher(@PathVariable Long voucherId) throws BaseException {
        voucherService.claimVoucher(voucherId);
        return ResponseEntity.ok().build();
    }
}
