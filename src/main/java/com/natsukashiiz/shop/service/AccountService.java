package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.redis.RedisService;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.PointRepository;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AuthService authService;
    private final RedisService redisService;
    private final AccountRepository accountRepository;
    private final PointRepository pointRepository;
    private final MailService mailService;

    public void getNewVerifyCode() throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("GetNewVerifyCode-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.send(current.getEmail(), "Verify Account", "Code: " + code);
        redisService.setValueByKey("ACCOUNT:CODE:" + current.getEmail(), code, Duration.ofMinutes(5).toMillis());
    }

    public void verify(String email) {
        accountRepository.verified(email);
    }
}
