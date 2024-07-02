package com.natsukashiiz.shop.api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.natsukashiiz.shop.common.SocialProviders;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.UserSocial;
import com.natsukashiiz.shop.exception.UserException;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.LoginException;
import com.natsukashiiz.shop.api.model.request.GoogleLoginRequest;
import com.natsukashiiz.shop.model.resposne.TokenResponse;
import com.natsukashiiz.shop.repository.UserRepository;
import com.natsukashiiz.shop.repository.UserSocialRepository;
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
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final GoogleIdTokenVerifier verifier;
    private final UserSocialRepository userSocialRepository;

    @Transactional
    public TokenResponse login(GoogleLoginRequest request, HttpServletRequest httpServletRequest) throws BaseException {

        if (ObjectUtils.isEmpty(request.getIdToken())) {
            log.warn("Login-[block]:(id token null). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }

        Optional<User> accountOptional = verifyIDToken(request.getIdToken());
        if (!accountOptional.isPresent()) {
            log.warn("Login-[block]:(invalid id token). request:{}", request.getIdToken());
            throw LoginException.invalid();
        }

        User googleUser = accountOptional.get();
        Optional<UserSocial> socialOptional = userSocialRepository.findByProviderAndEmail(SocialProviders.GOOGLE, googleUser.getEmail());

        User user;
        if (socialOptional.isPresent()) {
            user = socialOptional.get().getUser();
        } else {
            user = createOrUpdateUser(googleUser);
            UserSocial social = new UserSocial();
            social.setUser(user);
            social.setEmail(user.getEmail());
            social.setProvider(SocialProviders.GOOGLE);
            userSocialRepository.save(social);
        }

        if (user.getDeleted()) {
            log.warn("Login-[block]:(account deleted). account:{}", user);
            throw UserException.deleted();
        }

        return authService.createTokenResponse(user, httpServletRequest);
    }

    @Transactional
    public User createOrUpdateUser(User user) {
        Optional<User> accountOptional = userRepository.findByEmail(user.getEmail());
        if (!accountOptional.isPresent()) {
            userRepository.save(user);
            return user;
        } else {
            return accountOptional.get();
        }
    }

    public Optional<User> verifyIDToken(String idToken) {
        try {
            // TODO: bug if idToken invalid it Internal Server Error
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                log.warn("VerifyIDToken-[block]:(invalid googleIdToken). idToken:{}", idToken);
                return Optional.empty();
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            User user = new User();
            user.setEmail(payload.getEmail());

            do {
                user.setNickName(RandomUtils.randomNickName());
            } while (userRepository.existsByNickName(user.getNickName()));

            user.setPassword(passwordEncoder.encode(RandomUtils.notSymbol() + payload.getSubject()));
            user.setVerified(Boolean.TRUE);
            user.setDeleted(Boolean.FALSE);

            return Optional.of(user);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("VerifyIDToken-[block]. idToken:{}, error:{}", idToken, e.getMessage());
            return Optional.empty();
        }
    }
}
