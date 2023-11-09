package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Point;
import com.natsukashiiz.shop.exception.AuthException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.LoginException;
import com.natsukashiiz.shop.exception.SignUpException;
import com.natsukashiiz.shop.model.request.LoginRequest;
import com.natsukashiiz.shop.model.request.SignUpRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.PointRepository;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;

    public TokenResponse login(LoginRequest req) throws BaseException {
        if (ValidationUtils.invalidEmail(req.getEmail())) {
            log.warn("Login-[block]:(invalid email). req:{}", req);
            throw LoginException.emailInvalid();
        }

        Optional<Account> accountOptional = accountRepository.findByEmail(req.getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(not found). req:{}", req);
            throw LoginException.invalid();
        }

        Account account = accountOptional.get();
        if (!passwordMatch(req.getPassword(), account.getPassword())) {
            log.warn("Login-[block]:(password not matches). req:{}", req);
            throw LoginException.invalid();
        }

        return createTokenResponse(account);
    }

    public TokenResponse signUp(SignUpRequest req) throws BaseException {
        if (ValidationUtils.invalidEmail(req.getEmail())) {
            log.warn("SignUp-[block]:(invalid email). req:{}", req);
            throw LoginException.emailInvalid();
        }

        if (accountRepository.existsByEmail(req.getEmail())) {
            log.warn("SignUp-[block]:(exists email). req:{}", req);
            throw SignUpException.emailDuplicate();
        }

        Account account = new Account();
        account.setEmail(req.getEmail());
        account.setPassword(passwordEncoder.encode(req.getPassword()));
        accountRepository.save(account);

        Point point = new Point();
        point.setAccount(account);
        point.setPoint(0.00);
        pointRepository.save(point);

        return createTokenResponse(account);
    }

    public Account getCurrent() throws BaseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("GetCurrent-[block]:(authentication is null)");
            throw AuthException.unauthorized();
        }

        if (!authentication.isAuthenticated()) {
            log.warn("GetCurrent-[block]:(not authenticated)");
            throw AuthException.unauthorized();
        }

        Jwt jwt = (Jwt) authentication.getCredentials();

        if (jwt == null) {
            log.warn("GetCurrent-[block]:(jwt is null)");
            throw AuthException.unauthorized();
        }

        String accountId = authentication.getName();
        String email = jwt.getClaimAsString("email");

        if (!StringUtils.hasText(accountId) || !StringUtils.hasText(email)) {
            log.warn("GetCurrent-[block]:(accountId or email is null)");
            throw AuthException.unauthorized();
        }

        Optional<Account> accountOptional = accountRepository.findByIdAndEmail(Long.parseLong(accountId), email);
        if (!accountOptional.isPresent()) {
            log.warn("GetCurrent-[block]:(not found account). accountId:{}, email:{}", accountId, email);
            throw AuthException.unauthorized();
        }

        return accountOptional.get();
    }

    public boolean passwordMatch(String raw, String hash) {
        return passwordEncoder.matches(raw, hash);
    }

    private TokenResponse createTokenResponse(Account account) {
        return TokenResponse.builder()
                .token(tokenService.generate(account.getId(), account.getEmail()))
                .build();
    }
}
