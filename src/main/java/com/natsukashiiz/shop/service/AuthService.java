package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.model.request.LoginRequest;
import com.natsukashiiz.shop.model.request.SignUpRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<TokenResponse> login(LoginRequest req) {
        Optional<Account> accountOptional = accountRepository.findByEmail(req.getEmail());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(not found). req:{}", req);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Account account = accountOptional.get();
        if (!passwordEncoder.matches(req.getPassword(), account.getPassword())) {
            log.warn("Login-[block]:(password not matches). req:{}", req);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                TokenResponse.builder()
                        .token(tokenService.gen(req.getEmail()))
                        .build()
        );
    }

    public ResponseEntity<TokenResponse> signUp(SignUpRequest req) {
        if (accountRepository.existsByEmail(req.getEmail())) {
            log.warn("SignUp-[block]:(exists email). req:{}", req);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Account account = new Account();
        account.setEmail(req.getEmail());
        account.setPassword(passwordEncoder.encode(req.getPassword()));

        accountRepository.save(account);
        return ResponseEntity.ok(
                TokenResponse.builder()
                        .token(tokenService.gen(req.getEmail()))
                        .build()
        );
    }
}
