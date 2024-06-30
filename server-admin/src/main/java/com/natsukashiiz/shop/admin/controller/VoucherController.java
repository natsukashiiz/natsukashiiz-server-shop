package com.natsukashiiz.shop.admin.controller;

import com.natsukashiiz.shop.entity.Voucher;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vouchers")
@AllArgsConstructor
public class VoucherController {
    // query all, query by id, create, update, delete

    @GetMapping
    public ResponseEntity<?> queryVouchers() {
        return ResponseEntity.ok("Query all vouchers");
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<?> queryVoucherById(@PathVariable Long voucherId) {
        return ResponseEntity.ok(String.format("Query voucher by id: %d", voucherId));
    }

    @PostMapping
    public ResponseEntity<?> createVoucher(@RequestBody Voucher voucher) {
        return ResponseEntity.ok(String.format("Create voucher: %s", voucher));
    }

    @PutMapping("/{voucherId}")
    public ResponseEntity<?> updateVoucher(@PathVariable Long voucherId, @RequestBody Voucher voucher) {
        return ResponseEntity.ok(String.format("Update voucher by id: %d, %s", voucherId, voucher));
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Long voucherId) {
        return ResponseEntity.ok(String.format("Delete voucher by id: %d", voucherId));
    }
}
