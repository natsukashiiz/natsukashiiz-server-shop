package com.natsukashiiz.shop.utils;

import com.natsukashiiz.shop.entity.Account;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@AllArgsConstructor
@Log4j2
public class SecurityUtils {

    public static Account getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getCredentials();
        Account account = new Account();
        account.setId(Long.parseLong(jwt.getSubject()));
        account.setEmail((String) jwt.getClaims().get("email"));
        return account;
    }
}
