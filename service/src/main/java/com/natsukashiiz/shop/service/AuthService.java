package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.Roles;
import com.natsukashiiz.shop.common.ServerProperties;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.LoginHistory;
import com.natsukashiiz.shop.exception.*;
import com.natsukashiiz.shop.model.request.LoginRequest;
import com.natsukashiiz.shop.model.request.RefreshTokenRequest;
import com.natsukashiiz.shop.model.request.SignUpRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.LoginHistoryRepository;
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
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
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

        Optional<Account> accountOptional = accountRepository.findByEmail(request.getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(not found account). request:{}", request);
            throw LoginException.invalid();
        }

        Account account = accountOptional.get();
        if (passwordNotMatch(request.getPassword(), account.getPassword())) {
            log.warn("Login-[block]:(password not matches). request:{}", request);
            throw LoginException.invalid();
        }

        if (account.getDeleted()) {
            log.warn("Login-[block]:(account deleted). request:{}", request);
            throw AccountException.deleted();
        }

        return createTokenResponse(account, httpServletRequest);
    }

    public TokenResponse signUp(SignUpRequest request, HttpServletRequest httpServletRequest) throws BaseException {
        if (ValidationUtils.invalidEmail(request.getEmail())) {
            log.warn("SignUp-[block]:(invalid email). request:{}", request);
            throw LoginException.invalidEmail();
        }

        if (accountRepository.existsByEmail(request.getEmail())) {
            log.warn("SignUp-[block]:(exists email). request:{}", request);
            throw SignUpException.emailDuplicate();
        }

        Account account = new Account();
        account.setEmail(request.getEmail());

        do {
            account.setNickName(RandomUtils.randomNickName());
        } while (accountRepository.existsByNickName(account.getNickName()));

        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setVerified(Boolean.FALSE);
        account.setDeleted(Boolean.FALSE);
        accountRepository.save(account);

        String code = RandomUtils.Number6Characters();
        mailService.sendActiveAccount(account.getEmail(), code, serverProperties.getVerification().replace("{CODE}", code));

        String redisKey = RedisKeyUtils.accountVerifyCodeKey(account.getEmail());
        redisService.setValueByKey(redisKey, code, Duration.ofMinutes(5).toMillis());

        return createTokenResponse(account, httpServletRequest);
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

        Optional<Account> accountOptional = accountRepository.findByIdAndEmail(accountId, email);
        if (!accountOptional.isPresent()) {
            log.warn("Refresh-[block]:(not found account). email:{}, accountId:{}", email, accountId);
            throw AuthException.unauthorized();
        }

        Account account = accountOptional.get();
        if (!account.getVerified()) {
            log.warn("Refresh-[block]:(account not verify). email:{}, accountId:{}", email, accountId);
            throw AccountException.notVerify();
        }

        return createTokenResponse(account, httpServletRequest, false);
    }

    public Account getAccount() throws BaseException {
        return getAccount(true);
    }

    public Account getAccount(boolean checkVerified) throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetAccount-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetAccount-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = (Jwt) authentication.getCredentials();

        if (jwt == null) {
            log.warn("GetAccount-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        if (!tokenService.isAccessToken(jwt)) {
            log.warn("GetAccount-[block]:(not access token)");
            throw AuthException.unauthorized();
        }

        String accountId = authentication.getName();
        String email = jwt.getClaimAsString("email");

        if (ObjectUtils.isEmpty(accountId) || ObjectUtils.isEmpty(email)) {
            log.warn("GetAccount-[block]:(accountId or email is empty). accountId:{}, email:{}", accountId, email);
            throw AuthException.unauthorized();
        }

        Optional<Account> accountOptional = accountRepository.findByIdAndEmail(Long.parseLong(accountId), email);
        if (!accountOptional.isPresent()) {
            log.warn("GetAccount-[block]:(not found account). accountId:{}, email:{}", accountId, email);
            throw AuthException.unauthorized();
        }

        Account account = accountOptional.get();

        if (checkVerified) {
            if (!account.getVerified()) {
                log.warn("GetAccount-[block]:(account not verify). accountId:{}, email:{}", accountId, email);
                throw AccountException.notVerify();
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

    public TokenResponse createTokenResponse(Account account, HttpServletRequest httpServletRequest, boolean enableLoginLog) {

        String userAgent = ServletUtils.getUserAgent(httpServletRequest);
        String ipAddress = ServletUtils.getIpAddress(httpServletRequest);
        String deviceName = ServletUtils.getDeviceName(userAgent);
        String osName = ServletUtils.getOsName(userAgent);

        if (enableLoginLog) {
            LoginHistory loginHistory = new LoginHistory();
            loginHistory.setAccount(account);
            loginHistory.setIp(ipAddress);
            loginHistory.setUserAgent(userAgent);
            loginHistory.setDevice(deviceName);
            loginHistory.setOs(osName);
            loginHistoryRepository.save(loginHistory);
        }

        String accessToken = tokenService.generateAccessToken(account.getId(), account.getEmail(), account.getVerified(), Roles.USER);
        String refreshToken = tokenService.generateRefreshToken(account.getId(), account.getEmail());

        return TokenResponse.build(accessToken, refreshToken);
    }

    public TokenResponse createTokenResponse(Account account, HttpServletRequest httpServletRequest) {
        return createTokenResponse(account, httpServletRequest, true);
    }
}
