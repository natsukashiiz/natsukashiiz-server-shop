package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Point;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (!passwordEncoder.matches(req.getPassword(), account.getPassword())) {
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

    private TokenResponse createTokenResponse(Account account) {
        return TokenResponse.builder()
                .token(tokenService.gen(account.getId(), account.getEmail()))
                .build();
    }
}
