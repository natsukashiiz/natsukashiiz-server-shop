package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.ApiProperties;
import com.natsukashiiz.shop.common.TokenType;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final ApiProperties properties;

    public String generateAccessToken(Long id, String email, boolean verified) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getBaseUrl())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("verified", verified)
                .claim("type", TokenType.ACCESS)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Long id, String email) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getBaseUrl())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("type", TokenType.REFRESH)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt decode(String token) {
        return this.decoder.decode(token);
    }


    public boolean isAccessToken(Jwt jwt) {
        return Objects.equals(jwt.getClaim("type"), TokenType.ACCESS.name());
    }

    public boolean isRefreshToken(Jwt jwt) {
        return Objects.equals(jwt.getClaim("type"), TokenType.REFRESH.name());
    }
}
