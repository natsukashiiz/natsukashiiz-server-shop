package com.natsukashiiz.shop.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${shop.files.upload-dir}")
    private String fileUploadDir;

    @Value("${shop.files.allowed-types}")
    private List<String> fileAllowedTypes;

    @Value("${shop.files.base-url}")
    private String fileBaseUrl;

    public Path getFileUploadDir() {
        return Paths.get(fileUploadDir);
    }
}
