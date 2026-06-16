package com.odonto.odonto_system.config;

import com.odonto.odonto_system.user.User;
import com.odonto.odonto_system.user.UserRepository;
import com.odonto.odonto_system.user.UserRole;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminUserSeeder {

    @Bean
    ApplicationRunner seedAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@odonto.com";

            if (repo.findByEmail(adminEmail).isEmpty()) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password(encoder.encode("ClinicaSegura@2026"))
                        .fullname("Gabriel Mendes Admin")
                        .role(UserRole.ADMIN)
                        .active(true)
                        .build();

                repo.save(admin);
                System.out.println("=========================================================");
                System.out.println("======> USUÁRIO ADMIN INICIAL CRIADO COM SUCESSO! <======");
                System.out.println("=========================================================");
            }
        };
    }

}
