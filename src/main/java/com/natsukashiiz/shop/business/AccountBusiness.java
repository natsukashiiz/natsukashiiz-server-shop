package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.common.ApiProperties;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.model.request.ForgotPasswordRequest;
import com.natsukashiiz.shop.model.request.ResetPasswordRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
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
import org.springframework.util.ObjectUtils;

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
    private final ApiProperties apiProperties;

    private final String REDIS_KEY = "ACCOUNT:CODE:";

    public void getVerifyCode() throws BaseException {
        Account current = authService.getCurrent(false);
        if (current.getVerified()) {
            log.warn("GetVerifyCode-[block]:(account is verified). current:{}, ", current);
            throw AccountException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.sendActiveAccount(current.getEmail(), code, apiProperties.getVerification().replace("{CODE}", code));
        redisService.setValueByKey(REDIS_KEY + current.getEmail(), code, Duration.ofMinutes(15).toMillis());
    }

    public TokenResponse verify(String verifyCode) throws BaseException {
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

//        Point point = new Point();
//        point.setAccount(current);
//        point.setPoint(0.00);
//        pointService.create(point);

        redisService.deleteByKey(REDIS_KEY + current.getEmail());

        current.setVerified(Boolean.TRUE);
        return authService.createTokenResponse(current);
    }

    public void changePassword(ChangePasswordRequest request) throws BaseException {

        if (!authService.passwordMatch(request.getCurrent(), authService.getCurrent().getPassword())) {
            log.warn("ChangePassword-[block]:(invalid current password)");
            throw AccountException.invalidCurrentPassword();
        }

        String password = passwordEncoder.encode(request.getLatest());
        accountService.changePassword(authService.getCurrent().getId(), password);
    }

    public void forgotPassword(ForgotPasswordRequest request) throws BaseException {
        Account account = accountService.findByEmail(request.getEmail());
        String code = RandomUtils.notSymbol();
        String link = apiProperties.getResetPassword();
        link = link.replace("{EMAIL}", account.getEmail());
        link = link.replace("{CODE}", code);
        mailService.sendResetPassword(account.getEmail(), link);
        redisService.setValueByKey(REDIS_KEY + account.getEmail(), code, Duration.ofMinutes(5).toMillis());
    }

    public TokenResponse resetPassword(ResetPasswordRequest request) throws BaseException {

        if (ObjectUtils.isEmpty(request.getEmail())) {
            log.warn("ResetPassword-[block]:(invalid email). request:{}", request);
            throw AccountException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getCode())) {
            log.warn("ResetPassword-[block]:(invalid code). request:{}", request);
            throw AccountException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getPassword())) {
            log.warn("ResetPassword-[block]:(invalid password). request:{}", request);
            throw AccountException.invalid();
        }

        Account account = accountService.findByEmail(request.getEmail());
        String code = redisService.getValueByKey(REDIS_KEY + request.getEmail());

        if (!Objects.equals(code, request.getCode())) {
            log.warn("ResetPassword-[block]:(invalid code). request:{}", request);
            throw AccountException.invalidResetCode();
        }

        redisService.deleteByKey(REDIS_KEY + account.getEmail());

        String encoded = passwordEncoder.encode(request.getPassword());
        account.setPassword(encoded);
        Account update = accountService.createOrUpdate(account);
        return authService.createTokenResponse(update);
    }
}
