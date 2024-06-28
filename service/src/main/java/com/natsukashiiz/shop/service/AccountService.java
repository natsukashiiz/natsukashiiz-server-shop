package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;

    public Account findByEmail(String email) throws BaseException {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (!accountOptional.isPresent()) {
            throw AccountException.invalid();
        }

        return accountOptional.get();
    }

    public Account createOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public void verify(String email) {
        accountRepository.verified(email);
    }

    @Transactional
    public void changePassword(Long accountId, String password) {
        accountRepository.changePassword(password, accountId);
    }
}
