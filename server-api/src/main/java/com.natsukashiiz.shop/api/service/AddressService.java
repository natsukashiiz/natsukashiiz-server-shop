package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Address;
import com.natsukashiiz.shop.exception.AddressException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.request.CreateAddressRequest;
import com.natsukashiiz.shop.api.model.request.UpdateAddressRequest;
import com.natsukashiiz.shop.api.model.response.AddressResponse;
import com.natsukashiiz.shop.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class AddressService {

    private final AddressRepository addressRepository;
    private final AuthService authService;

    public AddressResponse queryMainAddress() throws BaseException {
        Account account = authService.getAccount();
        Optional<Address> addressOptional = addressRepository.findByAccountAndMainIsTrue(account);
        if (!addressOptional.isPresent()) {
            log.warn("QueryMainAddress-[block]:(not found main address). accountId:{}", account.getId());
            throw AddressException.invalid();
        }

        return AddressResponse.build(addressOptional.get());
    }

    public List<AddressResponse> queryAllAddress() throws BaseException {
        Account account = authService.getAccount();
        return addressRepository.findByAccount(account)
                .stream()
                .map(AddressResponse::build)
                .collect(Collectors.toList());
    }

    public AddressResponse createAddress(CreateAddressRequest request) throws BaseException {
        Account account = authService.getAccount();

        if (ObjectUtils.isEmpty(request.getFirstName())) {
            log.warn("CreateAddress-[block]:(invalid first name). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getLastName())) {
            log.warn("CreateAddress-[block]:(invalid last name). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getMobile())) {
            log.warn("CreateAddress-[block]:(invalid mobile). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getAddress())) {
            log.warn("CreateAddress-[block]:(invalid address). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        Address address = new Address();
        address.setAccount(account);
        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setMobile(request.getMobile());
        address.setAddress(request.getAddress());

        if (addressRepository.countByAccount(account) == 0) {
            address.setMain(Boolean.TRUE);
        } else {
            address.setMain(Boolean.FALSE);
        }

        return AddressResponse.build(addressRepository.save(address));
    }

    @Transactional(rollbackOn = BaseException.class)
    public AddressResponse updateAddress(UpdateAddressRequest request) throws BaseException {
        Account account = authService.getAccount();

        if (ObjectUtils.isEmpty(request.getId())) {
            log.warn("UpdateAddress-[block]:(invalid address id). request:{}, accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        Optional<Address> addressOptional = addressRepository.findByIdAndAccount(request.getId(), account);
        if (!addressOptional.isPresent()) {
            log.warn("UpdateAddress-[block]:(not found address). request:{} accountId:{}", request, account.getId());
            throw AddressException.invalid();
        }

        Address address = addressOptional.get();

        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setMobile(request.getMobile());
        address.setAddress(request.getAddress());

        if (request.isMain()) {
            Address main = addressRepository.findByAccountAndMainIsTrue(account).get();
            main.setMain(Boolean.FALSE);
            addressRepository.save(main);
        }

        address.setMain(request.isMain());

        return AddressResponse.build(addressRepository.save(address));
    }

    public void deleteAddressById(Long addressId) throws BaseException {
        Account account = authService.getAccount();
        if (!addressRepository.existsByIdAndAccount(addressId, account)) {
            log.warn("DeleteAddressById-[block]:(not found address). addressId:{}, accountId:{}", addressId, account.getId());
            throw AddressException.invalid();
        }

        addressRepository.deleteById(addressId);
    }

}
