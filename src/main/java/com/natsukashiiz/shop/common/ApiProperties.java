package com.natsukashiiz.shop.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApiProperties {

    @Value("${shop.web.url.base}")
    private String baseUrl;

    @Value("${shop.web.url.verification}")
    private String verification;

    @Value("${shop.web.url.resetPassword}")
    private String resetPassword;

    @Value("${shop.web.url.paymentReturn}")
    private String paymentReturn;
}
