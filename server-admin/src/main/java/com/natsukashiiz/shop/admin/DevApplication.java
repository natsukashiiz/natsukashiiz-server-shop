package com.natsukashiiz.shop.admin;

import com.natsukashiiz.shop.common.ServerProperties;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class DevApplication implements ApplicationRunner {

    private final ServerProperties serverProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Dev Application started");
    }
}
