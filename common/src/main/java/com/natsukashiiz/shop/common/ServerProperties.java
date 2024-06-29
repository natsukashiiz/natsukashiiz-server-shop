package com.natsukashiiz.shop.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@Component
@Getter
public class ServerProperties {

    @Value("${shop.web.url.base}")
    private String baseUrl;

    @Value("${shop.web.url.verification}")
    private String verification;

    @Value("${shop.web.url.resetPassword}")
    private String resetPassword;

    @Value("#{T(java.nio.file.Paths).get('${shop.files.upload-dir}')}")
    private Path fileUploadDir;

    @Value("${shop.files.allowed-types}")
    private List<String> fileAllowedTypes;

    @Value("${shop.files.base-url}")
    private String fileBaseUrl;

    @Value("${shop.jwt.expiration.access}")
    private Duration jwtAccessExpiration;

    @Value("${shop.jwt.expiration.refresh}")
    private Duration jwtRefreshExpiration;
}
