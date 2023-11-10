package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Point;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.redis.RedisService;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.PointRepository;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final PointRepository pointRepository;
    private final MailService mailService;
    private final RedisService redisService;
    private final AuthService authService;

    public void getNewVerifyCode() throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("GetNewVerifyCode-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.send(current.getEmail(), "Verify Account", "Code: " + code);
        redisService.setValueByKey("ACCOUNT:CODE:" + current.getEmail(), code, Duration.ofMinutes(1).toMillis());
    }

    @Transactional(rollbackOn = BaseException.class)
    public void verify(String verifyCode) throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("Verify-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = redisService.getValueByKey("ACCOUNT:CODE:" + current.getEmail());
        if (!Objects.equals(code, verifyCode)) {
            log.warn("Verify-[block]:(invalid verify code). code:{}, current:{}", code, current);
            throw AccountException.invalidVerifyCode();
        }

        accountRepository.verified(current.getEmail());

        Point point = new Point();
        point.setAccount(current);
        point.setPoint(0.00);
        pointRepository.save(point);

        redisService.deleteByKey("ACCOUNT:CODE:" + current.getEmail());
    }
}
