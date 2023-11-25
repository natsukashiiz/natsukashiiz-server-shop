package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Address;
import com.natsukashiiz.shop.exception.AddressException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class AddressService {

    private final AddressRepository addressRepository;

    public Address getMain(Account account) throws BaseException {
        Optional<Address> addressOptional = addressRepository.findByAccountAndMain(account, Boolean.TRUE);
        if (!addressOptional.isPresent()) {
            log.warn("GetMain-[block]:(not found main address). account:{}", account);
            throw AddressException.invalid();
        }

        return addressOptional.get();
    }

    public Address getById(Long addressId) throws BaseException {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (!addressOptional.isPresent()) {
            log.warn("GetById-[block]:(not found). addressId:{}", addressId);
            throw AddressException.invalid();
        }

        return addressOptional.get();
    }

    public List<Address> getAll(Account account) {
        return addressRepository.findByAccount(account);
    }

    public Address createOrUpdate(Address address) {
        return addressRepository.save(address);
    }

    public void removeMain(Account account) {
        Optional<Address> addressOptional = addressRepository.findByAccountAndMain(account, Boolean.TRUE);
        if (addressOptional.isPresent()) {
            Address address = addressOptional.get();
            address.setMain(Boolean.FALSE);
            addressRepository.save(address);
        }
    }

    @Transactional
    public void removeMainByAccountId(Long accountId) {
        addressRepository.removeMainByAccountId(accountId);
    }

    @Transactional
    public void setMain(Long addressId, Long accountId) {
        addressRepository.setMain(addressId, accountId);
    }

    public Long countBy(Account account) {
        return addressRepository.countByAccount(account);
    }

    public boolean exitsByIdAndAccount(Long addressId, Account account) {
        return addressRepository.existsByIdAndAccount(addressId, account);
    }

    public void delete(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
