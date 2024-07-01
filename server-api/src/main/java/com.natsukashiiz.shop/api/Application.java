package com.natsukashiiz.shop.api;

import com.natsukashiiz.shop.service.OmisePaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.natsukashiiz.shop.*")
@EntityScan(basePackages = "com.natsukashiiz.shop.*")
@EnableJpaRepositories(basePackages = "com.natsukashiiz.shop.*")
@EnableCaching
@EnableScheduling
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Value("${shop.payment.omise.pubKey}")
    String omisePubKey;

    @Value("${shop.payment.omise.secretKey}")
    String omiseSecretKey;

    @Value("${shop.web.url.paymentReturn}")
    String paymentReturnUrl;

    @Bean
    OmisePaymentService paymentService() {
        return new OmisePaymentService(omisePubKey, omiseSecretKey, paymentReturnUrl);
    }
}
