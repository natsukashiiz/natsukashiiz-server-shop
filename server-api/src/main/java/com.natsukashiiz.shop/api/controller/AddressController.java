package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.business.AddressBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CreateAddressRequest;
import com.natsukashiiz.shop.model.request.UpdateAddressRequest;
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

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateAddressRequest request) throws BaseException {
        return ResponseEntity.ok(addressBusiness.update(request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> delete(@PathVariable Long addressId) throws BaseException {
        addressBusiness.delete(addressId);
        return ResponseEntity.ok().build();
    }
}
