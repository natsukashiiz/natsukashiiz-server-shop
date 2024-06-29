package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.request.CreateAddressRequest;
import com.natsukashiiz.shop.api.model.request.UpdateAddressRequest;
import com.natsukashiiz.shop.api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Get All", description = "Get all address")
    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(addressService.queryAllAddress());
    }

    @Operation(summary = "Get Main", description = "Get main address")
    @GetMapping("/main")
    public ResponseEntity<?> getMain() throws BaseException {
        return ResponseEntity.ok(addressService.queryMainAddress());
    }

    @Operation(summary = "Create", description = "Create new address")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAddressRequest request) throws BaseException {
        return ResponseEntity.ok(addressService.createAddress(request));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateAddressRequest request) throws BaseException {
        return ResponseEntity.ok(addressService.updateAddress(request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> delete(@PathVariable Long addressId) throws BaseException {
        addressService.deleteAddressById(addressId);
        return ResponseEntity.ok().build();
    }
}
