package com.natsukashiiz.shop.api.payment;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class PaymentProperties {

    @Value("${shop.payment.omise.pubKey}")
    String pubKey;

    @Value("${shop.payment.omise.secretKey}")
    String secretKey;
}
