package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;

    public String generate(Long id, String email) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("natsukashiiz-server-shop")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(id))
                .claim("email", email)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
