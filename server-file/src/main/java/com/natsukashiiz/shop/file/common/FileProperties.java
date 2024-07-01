package com.natsukashiiz.shop.file.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@Getter
public class FileProperties {

    @Value("#{T(java.nio.file.Paths).get('${shop.files.upload-dir}')}")
    private Path fileUploadDir;

    @Value("${shop.files.allowed-types}")
    private List<String> fileAllowedTypes;

    @Value("${shop.files.base-url}")
    private String fileBaseUrl;
}
