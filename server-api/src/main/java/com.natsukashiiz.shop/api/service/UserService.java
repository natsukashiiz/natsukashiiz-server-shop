package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.common.ServerProperties;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.LoginHistory;
import com.natsukashiiz.shop.exception.UserException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.request.ChangePasswordRequest;
import com.natsukashiiz.shop.api.model.request.ForgotPasswordRequest;
import com.natsukashiiz.shop.api.model.request.ResetPasswordRequest;
import com.natsukashiiz.shop.api.model.request.UpdateProfileRequest;
import com.natsukashiiz.shop.api.model.response.*;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.model.resposne.TokenResponse;
import com.natsukashiiz.shop.repository.UserRepository;
import com.natsukashiiz.shop.repository.LoginHistoryRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final RedisService redisService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final ServerProperties serverProperties;
    private final LoginHistoryRepository loginHistoryRepository;

    public ProfileResponse queryProfile() throws BaseException {
        User user = authService.getUser();

        ProfileResponse response = ProfileResponse.build(user);
        response.setSocials(UserSocialResponse.buildList(user.getSocials()));

        return response;
    }

    public ProfileResponse updateProfile(UpdateProfileRequest request) throws BaseException {
        User user = authService.getUser();
        user.setAvatar(request.getAvatar());

        if (!Objects.equals(request.getNickName(), user.getNickName())) {
            if (userRepository.existsByNickName(request.getNickName())) {
                log.warn("UpdateProfile-[block]:(nickname is already exist). request:{}, accountId:{}", request, user.getId());
                throw UserException.nickNameExist();
            }
            user.setNickName(request.getNickName());
        }

        User update = userRepository.save(user);

        ProfileResponse response = ProfileResponse.build(update);
        response.setSocials(UserSocialResponse.buildList(update.getSocials()));

        return response;
    }

    public void sendVerifyCode() throws BaseException {
        User user = authService.getUser(false);
        if (user.getVerified()) {
            log.warn("SendVerifyCode-[block]:(account is verified). accountId:{}", user.getId());
            throw UserException.verified();
        }

        String code = RandomUtils.Number6Characters();
        mailService.sendActiveUser(user.getEmail(), code, serverProperties.getVerification().replace("{CODE}", code));

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(user.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(15).toMillis());
    }

    public TokenResponse verifyCode(String verifyCode, HttpServletRequest httpServletRequest) throws BaseException {
        User user = authService.getUser(false);
        if (user.getVerified()) {
            log.warn("VerifyCode-[block]:(account is verified). accountId:{}", user.getId());
            throw UserException.verified();
        }

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(user.getEmail());
        String code = redisService.getValueByKey(redisKey);
        if (!Objects.equals(code, verifyCode)) {
            log.warn("VerifyCode-[block]:(invalid verify code). code:{}, accountId:{}", verifyCode, user.getId());
            throw UserException.invalidVerifyCode();
        }

        userRepository.verified(user.getEmail());

//        Point point = new Point();
//        point.setUser(current);
//        point.setPoint(0.00);
//        pointService.create(point);

        redisService.deleteByKey(redisKey);

        user.setVerified(Boolean.TRUE);
        return authService.createTokenResponse(user, httpServletRequest);
    }

    public void changePassword(ChangePasswordRequest request) throws BaseException {
        User user = authService.getUser();

        if (authService.passwordNotMatch(request.getCurrent(), user.getPassword())) {
            log.warn("ChangePassword-[block]:(invalid current password). accountId:{}", user.getId());
            throw UserException.invalidCurrentPassword();
        }

        String password = passwordEncoder.encode(request.getLatest());
        userRepository.changePassword(password, user.getId());
    }

    public void forgotPassword(ForgotPasswordRequest request) throws BaseException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserException::invalid);

        String code = RandomUtils.notSymbol();
        String link = serverProperties.getResetPassword();
        link = link.replace("{EMAIL}", user.getEmail());
        link = link.replace("{CODE}", code);
        mailService.sendResetPassword(user.getEmail(), link);

        String redisKey = RedisKeyUtils.accountResetPasswordCodeKey(user.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(5).toMillis());
    }

    public TokenResponse resetPassword(ResetPasswordRequest request, HttpServletRequest httpServletRequest) throws BaseException {

        if (ObjectUtils.isEmpty(request.getEmail())) {
            log.warn("ResetPassword-[block]:(invalid email). request:{}", request);
            throw UserException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getCode())) {
            log.warn("ResetPassword-[block]:(invalid code). request:{}", request);
            throw UserException.invalid();
        }

        if (ObjectUtils.isEmpty(request.getPassword())) {
            log.warn("ResetPassword-[block]:(invalid password). request:{}", request);
            throw UserException.invalid();
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(UserException::invalid);

        String redisKey = RedisKeyUtils.accountResetPasswordCodeKey(request.getEmail());
        String code = redisService.getValueByKey(redisKey);
        if (!Objects.equals(code, request.getCode())) {
            log.warn("ResetPassword-[block]:(invalid code). request:{}", request);
            throw UserException.invalidResetCode();
        }

        redisService.deleteByKey(redisKey);

        String encoded = passwordEncoder.encode(request.getPassword());
        user.setPassword(encoded);
        User update = userRepository.save(user);
        return authService.createTokenResponse(update, httpServletRequest);
    }

    public ProfileResponse deleteAvatar() throws BaseException {
        User user = authService.getUser();

        user.setAvatar(null);
        User update = userRepository.save(user);

        ProfileResponse response = ProfileResponse.build(update);
        response.setSocials(UserSocialResponse.buildList(update.getSocials()));

        return response;
    }

    public void deleteUser() throws BaseException {
        User user = authService.getUser();
        user.setDeleted(Boolean.TRUE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public PageResponse<List<LoginHistoryResponse>> queryLoginHistory(PaginationRequest pagination) throws BaseException {
        User user = authService.getUser();
        Page<LoginHistory> page = loginHistoryRepository.findByUserId(user.getId(), pagination);
        List<LoginHistoryResponse> responses = LoginHistoryResponse.buildList(page.getContent());
        return new PageResponse<>(responses, page.getTotalElements());
    }

}
