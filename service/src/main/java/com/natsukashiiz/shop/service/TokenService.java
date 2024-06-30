package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.ServerProperties;
import com.natsukashiiz.shop.common.Roles;
import com.natsukashiiz.shop.common.TokenType;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final ServerProperties properties;

    public String generateAccessToken(Long id, Map<String, Object> claims) {
        Instant now = Instant.now();

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuer(properties.getBaseUrl())
                .issuedAt(now)
                .expiresAt(now.plus(properties.getJwtAccessExpiration()))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(id));

        if (claims != null) {
            claims.forEach(builder::claim);
        }

        JwtClaimsSet jwtClaimsSet = builder.build();
        return this.encoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String generateRefreshToken(Long id) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getBaseUrl())
                .issuedAt(now)
                .expiresAt(now.plus(properties.getJwtRefreshExpiration()))
                .id(RandomUtils.notSymbol())
                .subject(String.valueOf(id))
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
