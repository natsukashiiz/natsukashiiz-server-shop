package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.common.ServerProperties;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.LoginHistory;
import com.natsukashiiz.shop.exception.AccountException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.api.model.request.ForgotPasswordRequest;
import com.natsukashiiz.shop.api.model.request.ResetPasswordRequest;
import com.natsukashiiz.shop.api.model.request.UpdateProfileRequest;
import com.natsukashiiz.shop.api.model.response.*;
import com.natsukashiiz.shop.model.resposne.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.LoginHistoryRepository;
import com.natsukashiiz.shop.service.FileService;
import com.natsukashiiz.shop.service.MailService;
import com.natsukashiiz.shop.service.RedisService;
import com.natsukashiiz.shop.utils.RandomUtils;
import com.natsukashiiz.shop.utils.RedisKeyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final RedisService redisService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ServerProperties serverProperties;
    private final LoginHistoryRepository loginHistoryRepository;
    private final FileService fileService;

    public ProfileResponse queryProfile() throws BaseException {
        Account account = authService.getAccount();

        ProfileResponse response = ProfileResponse.build(account);
        response.setSocials(AccountSocialResponse.buildList(account.getSocials()));

        return response;
    }

    public ProfileResponse updateProfile(UpdateProfileRequest request) throws BaseException {
        Account account = authService.getAccount();
        account.setAvatar(request.getAvatar());

        if (!Objects.equals(request.getNickName(), account.getNickName())) {
            if (accountRepository.existsByNickName(request.getNickName())) {
                log.warn("UpdateProfile-[block]:(nickname is already exist). request:{}, accountId:{}", request, account.getId());
                throw AccountException.nickNameExist();
            }
            account.setNickName(request.getNickName());
        }

        Account update = accountRepository.save(account);

        ProfileResponse response = ProfileResponse.build(update);
        response.setSocials(AccountSocialResponse.buildList(update.getSocials()));

        return response;
    }

    public void sendVerifyCode() throws BaseException {
        Account account = authService.getAccount(false);
        if (account.getVerified()) {
            log.warn("SendVerifyCode-[block]:(account is verified). accountId:{}", account.getId());
            throw AccountException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.sendActiveAccount(account.getEmail(), code, serverProperties.getVerification().replace("{CODE}", code));

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(account.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(15).toMillis());
    }

    public TokenResponse verifyCode(String verifyCode, HttpServletRequest httpServletRequest) throws BaseException {
        Account account = authService.getAccount(false);
        if (account.getVerified()) {
            log.warn("VerifyCode-[block]:(account is verified). accountId:{}", account.getId());
            throw AccountException.verified();
        }

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(account.getEmail());
        String code = redisService.getValueByKey(redisKey);
        if (!Objects.equals(code, verifyCode)) {
            log.warn("VerifyCode-[block]:(invalid verify code). code:{}, accountId:{}", verifyCode, account.getId());
            throw AccountException.invalidVerifyCode();
        }

        accountRepository.verified(account.getEmail());

//        Point point = new Point();
//        point.setAccount(current);
//        point.setPoint(0.00);
//        pointService.create(point);

        redisService.deleteByKey(redisKey);

        account.setVerified(Boolean.TRUE);
        return authService.createTokenResponse(account, httpServletRequest);
    }

    public void changePassword(ChangePasswordRequest request) throws BaseException {
        Account account = authService.getAccount();

        if (authService.passwordNotMatch(request.getCurrent(), account.getPassword())) {
            log.warn("ChangePassword-[block]:(invalid current password). accountId:{}", account.getId());
            throw AccountException.invalidCurrentPassword();
        }

        String password = passwordEncoder.encode(request.getLatest());
        accountRepository.changePassword(password, account.getId());
    }

    public void forgotPassword(ForgotPasswordRequest request) throws BaseException {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(AccountException::invalid);

        String code = RandomUtils.notSymbol();
        String link = serverProperties.getResetPassword();
        link = link.replace("{EMAIL}", account.getEmail());
        link = link.replace("{CODE}", code);
        mailService.sendResetPassword(account.getEmail(), link);

        String redisKey = RedisKeyUtils.accountResetPasswordCodeKey(account.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(5).toMillis());
    }

    public TokenResponse resetPassword(ResetPasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {

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

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(AccountException::invalid);

        String redisKey = RedisKeyUtils.accountResetPasswordCodeKey(request.getEmail());
        String code = redisService.getValueByKey(redisKey);
        if (!Objects.equals(code, request.getCode())) {
            log.warn("ResetPassword-[block]:(invalid code). request:{}", request);
            throw AccountException.invalidResetCode();
        }

        redisService.deleteByKey(redisKey);

        String encoded = passwordEncoder.encode(request.getPassword());
        account.setPassword(encoded);
        Account update = accountRepository.save(account);
        return authService.createTokenResponse(update, httpServletRequest);
    }

    public ProfileResponse deleteAvatar() throws BaseException {
        Account account = authService.getAccount();

        fileService.deleteWithUrl(account.getAvatar());

        account.setAvatar(null);
        Account update = accountRepository.save(account);

        ProfileResponse response = ProfileResponse.build(update);
        response.setSocials(AccountSocialResponse.buildList(update.getSocials()));

        return response;
    }

    public void deleteAccount() throws BaseException {
        Account account = authService.getAccount();
        account.setDeleted(Boolean.TRUE);
        account.setDeletedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    public PageResponse<List<LoginHistoryResponse>> queryLoginHistory(PaginationRequest pagination) throws BaseException {
        Account account = authService.getAccount();
        Page<LoginHistory> page = loginHistoryRepository.findByAccountId(account.getId(), pagination);
        List<LoginHistoryResponse> responses = LoginHistoryResponse.buildList(page.getContent());
        return new PageResponse<>(responses, page.getTotalElements());
    }

}
