package com.natsukashiiz.shop;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.MailService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEmailService {

    @Autowired
    private MailService mailService;

    @Order(1)
    @Test
    void test() throws BaseException {
        mailService.sendActiveAccount(
                DATA.email,
                DATA.code,
                DATA.link
        );
    }

    interface DATA {
        String email = "natsukashiiz.dev@gmail.com";
        String code = "123456";
        String link = "http://localhost:4200/verification?code=123456";
    }
}
