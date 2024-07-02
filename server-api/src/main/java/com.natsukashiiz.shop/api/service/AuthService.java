package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.api.model.request.LoginRequest;
import com.natsukashiiz.shop.api.model.request.SignUpRequest;
import com.natsukashiiz.shop.common.ServerProperties;
import com.natsukashiiz.shop.entity.LoginHistory;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.exception.*;
import com.natsukashiiz.shop.model.request.RefreshTokenRequest;
import com.natsukashiiz.shop.model.resposne.TokenResponse;
import com.natsukashiiz.shop.repository.LoginHistoryRepository;
import com.natsukashiiz.shop.repository.UserRepository;
import com.natsukashiiz.shop.service.MailService;
import com.natsukashiiz.shop.service.RedisService;
import com.natsukashiiz.shop.service.TokenService;
import com.natsukashiiz.shop.utils.RandomUtils;
import com.natsukashiiz.shop.utils.RedisKeyUtils;
import com.natsukashiiz.shop.utils.ServletUtils;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RedisService redisService;
    private final ServerProperties serverProperties;
    private final LoginHistoryRepository loginHistoryRepository;

    public TokenResponse login(LoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        if (ValidationUtils.invalidEmail(request.getEmail())) {
            log.warn("Login-[block]:(invalid email). request:{}", request);
            throw LoginException.invalidEmail();
        }

        Optional<User> accountOptional = userRepository.findByEmail(request.getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(not found account). request:{}", request);
            throw LoginException.invalid();
        }

        User user = accountOptional.get();
        if (passwordNotMatch(request.getPassword(), user.getPassword())) {
            log.warn("Login-[block]:(password not matches). request:{}", request);
            throw LoginException.invalid();
        }

        if (user.getDeleted()) {
            log.warn("Login-[block]:(account deleted). request:{}", request);
            throw UserException.deleted();
        }

        return createTokenResponse(user, httpServletRequest);
    }

    public TokenResponse signUp(SignUpRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        if (ValidationUtils.invalidEmail(request.getEmail())) {
            log.warn("SignUp-[block]:(invalid email). request:{}", request);
            throw LoginException.invalidEmail();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("SignUp-[block]:(exists email). request:{}", request);
            throw SignUpException.emailDuplicate();
        }

        User user = new User();
        user.setEmail(request.getEmail());

        do {
            user.setNickName(RandomUtils.randomNickName());
        } while (userRepository.existsByNickName(user.getNickName()));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerified(Boolean.FALSE);
        user.setDeleted(Boolean.FALSE);
        userRepository.save(user);

        String code = RandomUtils.Number6Characters();
        mailService.sendActiveUser(user.getEmail(), code, serverProperties.getVerification().replace("{CODE}", code));

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(user.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(5).toMillis());

        return createTokenResponse(user, httpServletRequest);
    }

    public TokenResponse refresh(RefreshTokenRequest request, HttpServletRequest httpServletRequest) throws BaseException {

        if (ObjectUtils.isEmpty(request.getRefreshToken())) {
            log.warn("Refresh-[block]:(refresh token is empty)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = tokenService.decode(request.getRefreshToken());

        if (jwt == null) {
            log.warn("Refresh-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        Long accountId = Long.parseLong(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        if (!tokenService.isRefreshToken(jwt)) {
            log.warn("Refresh-[block]:(not refresh token)");
            throw AuthException.unauthorized();
        }

        if (ObjectUtils.isEmpty(email) || ObjectUtils.isEmpty(accountId)) {
            log.warn("Refresh-[block]:(email or accountId is empty). email:{}, accountId:{}", email, accountId);
            throw AuthException.unauthorized();
        }

        Optional<User> accountOptional = userRepository.findByIdAndEmail(accountId, email);
        if (!accountOptional.isPresent()) {
            log.warn("Refresh-[block]:(not found account). email:{}, accountId:{}", email, accountId);
            throw AuthException.unauthorized();
        }

        User user = accountOptional.get();
        if (!user.getVerified()) {
            log.warn("Refresh-[block]:(account not verify). email:{}, accountId:{}", email, accountId);
            throw UserException.notVerify();
        }

        return createTokenResponse(user, httpServletRequest, false);
    }

    public User getUser() throws BaseException {
        return getUser(true);
    }

    public User getUser(boolean checkVerified) throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetUser-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetUser-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = (Jwt) authentication.getCredentials();

        if (jwt == null) {
            log.warn("GetUser-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        if (!tokenService.isAccessToken(jwt)) {
            log.warn("GetUser-[block]:(not access token)");
            throw AuthException.unauthorized();
        }

        String accountId = authentication.getName();

        if (ObjectUtils.isEmpty(accountId)) {
            log.warn("GetUser-[block]:(accountId is empty)");
            throw AuthException.unauthorized();
        }

        Optional<User> accountOptional = userRepository.findById(Long.parseLong(accountId));
        if (!accountOptional.isPresent()) {
            log.warn("GetUser-[block]:(not found account). accountId:{}", accountId);
            throw AuthException.unauthorized();
        }

        User user = accountOptional.get();

        if (checkVerified) {
            if (!user.getVerified()) {
                log.warn("GetUser-[block]:(account not verify). accountId:{}", accountId);
                throw UserException.notVerify();
            }
        }

        return accountOptional.get();
    }

    public boolean anonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ObjectUtils.isEmpty(authentication) || authentication.getPrincipal().equals("anonymousUser");
    }

    public boolean passwordNotMatch(String raw, String hash) {
        return !passwordEncoder.matches(raw, hash);
    }

    public TokenResponse createTokenResponse(User user, HttpServletRequest httpServletRequest, boolean enableLoginLog) {

        String userAgent = ServletUtils.getUserAgent(httpServletRequest);
        String ipAddress = ServletUtils.getIpAddress(httpServletRequest);
        String deviceName = ServletUtils.getDeviceName(userAgent);
        String osName = ServletUtils.getOsName(userAgent);

        if (enableLoginLog) {
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setUser(user);
            loginHistory.setIp(ipAddress);
            loginHistory.setUserAgent(userAgent);
            loginHistory.setDevice(deviceName);
            loginHistory.setOs(osName);
            loginHistoryRepository.save(loginHistory);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("verified", user.getVerified());

        String accessToken = tokenService.generateAccessToken(user.getId(), claims);
        String refreshToken = tokenService.generateRefreshToken(user.getId());

        return TokenResponse.build(accessToken, refreshToken);
    }

    public TokenResponse createTokenResponse(User user, HttpServletRequest httpServletRequest) {
        return createTokenResponse(user, httpServletRequest, true);
    }
}
