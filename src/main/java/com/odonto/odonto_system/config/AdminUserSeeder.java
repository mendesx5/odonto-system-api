package com.odonto.odonto_system.config;

import com.odonto.odonto_system.user.User;
import com.odonto.odonto_system.user.UserRepository;
import com.odonto.odonto_system.user.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserSeeder {

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    ApplicationRunner seedAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail(adminEmail).isEmpty()) {
                User admin = User.builder()
                        .email(adminEmail)
                        .password(encoder.encode(adminPassword))
                        .fullName("Gabriel Mendes Admin")
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
