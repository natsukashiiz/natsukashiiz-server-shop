package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.entity.Address;
import com.natsukashiiz.shop.exception.AddressException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.CreateAddressRequest;
import com.natsukashiiz.shop.model.response.AddressResponse;
import com.natsukashiiz.shop.service.AddressService;
import com.natsukashiiz.shop.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AddressBusiness {

    private final AuthService authService;
    private final AddressService addressService;

    public AddressResponse getMain() throws BaseException {
        Address main = addressService.getMain(authService.getCurrent());
        return AddressResponse.build(main);
    }

    public List<AddressResponse> getAll() throws BaseException {
        return addressService.getAll(authService.getCurrent())
                .stream()
                .map(AddressResponse::build)
                .collect(Collectors.toList());
    }

    public AddressResponse create(CreateAddressRequest request) throws BaseException {

        if (ObjectUtils.isEmpty(request.getFirstName())) {
            log.warn("Create-[block]:(invalid first name). request:{}", request);
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getLastName())) {
            log.warn("Create-[block]:(invalid last name). request:{}", request);
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getMobile())) {
            log.warn("Create-[block]:(invalid mobile). request:{}", request);
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getAddress())) {
            log.warn("Create-[block]:(invalid address). request:{}", request);
            throw AddressException.invalid();
        }

        Address address = new Address();
        address.setAccount(authService.getCurrent());
        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setMobile(request.getMobile());
        address.setAddress(request.getAddress());

        if (addressService.countBy(authService.getCurrent()) == 0) {
            address.setMain(Boolean.TRUE);
        } else {
            address.setMain(Boolean.FALSE);
        }

        return AddressResponse.build(addressService.createOrUpdate(address));
    }

    public AddressResponse setMain(Long addressId) throws BaseException {
        if (ObjectUtils.isEmpty(addressId)) {
            log.warn("SetMain-[block]:(invalid mobile). addressId:{}", addressId);
            throw AddressException.invalid();
        }

        addressService.removeMainByAccountId(authService.getCurrent().getId());
        addressService.setMain(addressId, authService.getCurrent().getId());

        return AddressResponse.build(addressService.getById(addressId));
    }
}
