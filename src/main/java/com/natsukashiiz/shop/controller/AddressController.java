package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.AddressBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CreateAddressRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressBusiness addressBusiness;

    @Operation(summary = "Get All", description = "Get all address")
    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(addressBusiness.getAll());
    }

    @Operation(summary = "Get Main", description = "Get main address")
    @GetMapping("/main")
    public ResponseEntity<?> getMain() throws BaseException {
        return ResponseEntity.ok(addressBusiness.getMain());
    }

    @Operation(summary = "Create", description = "Create new address")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAddressRequest request) throws BaseException {
        return ResponseEntity.ok(addressBusiness.create(request));
    }

    @Operation(summary = "Set Main", description = "Set main address")
    @PatchMapping("/main/{addressId}")
    public ResponseEntity<?> setMain(@PathVariable Long addressId) throws BaseException {
        return ResponseEntity.ok(addressBusiness.setMain(addressId));
    }
}
