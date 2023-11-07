package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.WalletException;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.repository.WalletRepository;
import com.natsukashiiz.shop.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class WalletService {
    private final WalletRepository walletRepository;

    public ResponseEntity<WalletResponse> create() throws BaseException {
        Account auth = SecurityUtils.getAuth();
        if (walletRepository.existsByAccount(auth)) {
            log.warn("Create-[block]-(exists). auth:{}", auth);
            throw WalletException.invalid();
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(0.00);
        wallet.setAccount(auth);

        wallet = walletRepository.save(wallet);
        return ResponseEntity.ok(
                WalletResponse.builder()
                        .id(wallet.getId())
                        .balance(wallet.getBalance())
                        .build()
        );
    }

    public ResponseEntity<WalletResponse> get() throws BaseException {
        Account auth = SecurityUtils.getAuth();

        Optional<Wallet> walletOptional = walletRepository.findByAccount(auth);

        if (!walletOptional.isPresent()) {
            log.warn("Get-[block]-(not found). auth:{}", auth);
            throw WalletException.invalid();
        }

        Wallet wallet = walletOptional.get();
        return ResponseEntity.ok(
                WalletResponse.builder()
                        .id(wallet.getId())
                        .balance(wallet.getBalance())
                        .build()
        );
    }
}
