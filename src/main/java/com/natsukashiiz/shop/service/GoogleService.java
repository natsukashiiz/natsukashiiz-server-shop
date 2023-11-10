package com.natsukashiiz.shop.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.LoginException;
import com.natsukashiiz.shop.model.request.GoogleLoginRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class GoogleService {
    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier verifier;

    public TokenResponse login(GoogleLoginRequest request) throws BaseException {

        if (!StringUtils.hasText(request.getIdToken())) {
            log.warn("Login-[block]:(id token null). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }

        Optional<Account> accountOptional = verifyIDToken(request.getIdToken());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(invalid id token). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }
        Account account = createOrUpdateAccount(accountOptional.get());
        return authService.createTokenResponse(account);
    }

    @Transactional
    public Account createOrUpdateAccount(Account account) {
        Optional<Account> accountOptional = accountRepository.findByEmail(account.getEmail());
        if (!accountOptional.isPresent()) {
            accountRepository.save(account);
            return account;
        } else {
            return accountOptional.get();
        }
    }

    public Optional<Account> verifyIDToken(String idToken) {
        try {
            // TODO: bug if idToken invalid it Internal Server Error
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                log.warn("VerifyIDToken-[block]:(googleIdToken null). idToken:{}", idToken);
                return Optional.empty();
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            Account account = new Account();
            account.setEmail(payload.getEmail());
            account.setPassword(passwordEncoder.encode(RandomUtils.UUIDNotDash() + payload.getSubject()));
            account.setVerified(Boolean.TRUE);

            return Optional.of(account);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("VerifyIDToken-[block]. idToken:{}, error:{}", idToken, e.getMessage());
            return Optional.empty();
        }
    }
}
