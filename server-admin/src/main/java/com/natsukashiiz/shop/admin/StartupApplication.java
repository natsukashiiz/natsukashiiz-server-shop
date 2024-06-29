package com.natsukashiiz.shop.admin;

import com.natsukashiiz.shop.common.AdminRoles;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.repository.AdminRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class StartupApplication {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            log.debug("Application Running...");

            long count = adminRepository.count();
            if (count == 0) {
                log.debug("No admin user found, creating default admin user...");

                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(AdminRoles.SUPER_ADMIN);
                adminRepository.save(admin);

                log.debug("Default admin user created.");
            }
        };
    }
}
