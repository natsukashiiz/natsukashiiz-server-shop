package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Point;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.redis.RedisService;
import com.natsukashiiz.shop.service.AccountService;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.MailService;
import com.natsukashiiz.shop.service.PointService;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class AccountBusiness {

    private final AuthService authService;
    private final RedisService redisService;
    private final AccountService accountService;
    private final PointService pointService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    private final String REDIS_KEY = "ACCOUNT:CODE:";

    public void getVerifyCode() throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("GetVerifyCode-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.send(current.getEmail(), "Verify Account", "Code: " + code);
        redisService.setValueByKey(REDIS_KEY + current.getEmail(), code, Duration.ofMinutes(5).toMillis());
    }

    public void verify(String verifyCode) throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("Verify-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = redisService.getValueByKey(REDIS_KEY + current.getEmail());
        if (!Objects.equals(code, verifyCode)) {
            log.warn("Verify-[block]:(invalid verify code). code:{}, current:{}", code, current);
            throw AccountException.invalidVerifyCode();
        }

        accountService.verify(current.getEmail());

        Point point = new Point();
        point.setAccount(current);
        point.setPoint(0.00);
        pointService.create(point);

        redisService.deleteByKey(REDIS_KEY + current.getEmail());
    }

    public void changePassword(ChangePasswordRequest request) throws BaseException {

        if (!authService.passwordMatch(request.getCurrent(), authService.getCurrent().getPassword())) {
            log.warn("ChangePassword-[block]:(invalid current password)");
            throw AccountException.invalidCurrentPassword();
        }

        String password = passwordEncoder.encode(request.getLatest());
        accountService.changePassword(authService.getCurrent().getId(), password);
    }
}
