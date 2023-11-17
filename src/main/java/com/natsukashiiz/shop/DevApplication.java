package com.natsukashiiz.shop;

import com.natsukashiiz.shop.payment.PaymentProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Log4j2
public class DevApplication implements ApplicationRunner {

    @Resource
    private PaymentProperties payment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("DevApplication running...");
//        payment.chargeWithToken();
    }
}
