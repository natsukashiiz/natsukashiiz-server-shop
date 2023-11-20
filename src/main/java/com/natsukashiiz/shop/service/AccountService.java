package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void verify(String email) {
        accountRepository.verified(email);
    }

    @Transactional
    public void changePassword(Long accountId, String password) {
        accountRepository.changePassword(password, accountId);
    }
}
