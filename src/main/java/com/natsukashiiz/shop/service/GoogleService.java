package com.natsukashiiz.shop.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.natsukashiiz.shop.common.SocialProvider;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.AccountSocial;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.LoginException;
import com.natsukashiiz.shop.model.request.GoogleLoginRequest;
import com.natsukashiiz.shop.model.response.TokenResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.AccountSocialRepository;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
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
    private final AccountSocialRepository accountSocialRepository;

    @Transactional
    public TokenResponse login(GoogleLoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {

        if (ObjectUtils.isEmpty(request.getIdToken())) {
            log.warn("Login-[block]:(id token null). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }

        Optional<Account> accountOptional = verifyIDToken(request.getIdToken());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(invalid id token). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }


        Account googleAccount = accountOptional.get();
        Optional<AccountSocial> socialOptional = accountSocialRepository.findByProviderAndEmail(SocialProvider.GOOGLE, googleAccount.getEmail());

        Account account;
        if (socialOptional.isPresent()) {
            account = socialOptional.get().getAccount();
        } else {
            account = createOrUpdateAccount(googleAccount);
            AccountSocial social = new AccountSocial();
            social.setAccount(account);
            social.setEmail(account.getEmail());
            social.setProvider(SocialProvider.GOOGLE);
            accountSocialRepository.save(social);
        }

        return authService.createTokenResponse(account, httpServletRequest);
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
                log.warn("VerifyIDToken-[block]:(invalid googleIdToken). idToken:{}", idToken);
                return Optional.empty();
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            Account account = new Account();
            account.setEmail(payload.getEmail());
            account.setPassword(passwordEncoder.encode(RandomUtils.notSymbol() + payload.getSubject()));
            account.setVerified(Boolean.TRUE);

            return Optional.of(account);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("VerifyIDToken-[block]. idToken:{}, error:{}", idToken, e.getMessage());
            return Optional.empty();
        }
    }
}
