package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class WalletService {
    private final WalletRepository walletRepository;
    private final AccountRepository accountRepository;

    public ResponseEntity<WalletResponse> create(Authentication authentication) {
        Account account = accountRepository.findByEmail(authentication.getName()).get();
        if (walletRepository.existsByAccount(account)) {
            log.warn("Create-[block]-(not found). email:{}", authentication.getName());
            return ResponseEntity.badRequest().build();
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(0.00);
        wallet.setAccount(account);

        wallet = walletRepository.save(wallet);
        return ResponseEntity.ok(
                WalletResponse.builder()
                        .id(wallet.getId())
                        .balance(wallet.getBalance())
                        .build()
        );
    }

    public ResponseEntity<WalletResponse> get(Authentication authentication) {
        Optional<Wallet> walletOptional = walletRepository.findByAccount(accountRepository.findByEmail(authentication.getName()).get());

        if (!walletOptional.isPresent()) {
            log.warn("Get-[block]-(not found). email:{}", authentication.getName());
            return ResponseEntity.badRequest().build();
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
