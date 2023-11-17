package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Wallet;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.WalletException;
import com.natsukashiiz.shop.model.request.TopUpRequest;
import com.natsukashiiz.shop.model.response.WalletResponse;
import com.natsukashiiz.shop.repository.WalletRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletResponse myWallet(Account account) throws BaseException {
        Optional<Wallet> walletOptional = walletRepository.findByAccount(account);

        if (!walletOptional.isPresent()) {
            log.warn("Get-[block]-(not found). account:{}", account);
            throw WalletException.invalid();
        }

        Wallet wallet = walletOptional.get();
        return WalletResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }

    public WalletResponse create(Account account) throws BaseException {
        if (walletRepository.existsByAccount(account)) {
            log.warn("Create-[block]-(exists). account:{}", account);
            throw WalletException.invalid();
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(0.00);
        wallet.setAccount(account);

        wallet = walletRepository.save(wallet);
        return WalletResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .build();
    }

    @Transactional
    public WalletResponse topUp(TopUpRequest request, Account account) throws BaseException {
        walletRepository.increaseBalance(account.getId(), request.getAmount());
        return myWallet(account);
    }
}
