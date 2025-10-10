package com.example.user_srv.demo;

import com.example.user_srv.constant_.Constants;
import com.example.user_srv.entity.Role;
import com.example.user_srv.entity.User;
import com.example.user_srv.repository.RoleRepository;
import com.example.user_srv.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository,
            com.example.user_srv.utils.AESHelper aesHelper) {
        return args -> {

            // 1. Tạo role ADMIN nếu chưa tồn tại
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder()
                            .name("ADMIN")
                            .build()));

            // 2. Tạo user admin nếu chưa tồn tại
            if (userRepository.findByUsername("admin").isEmpty()) {

                // Encrypt password bằng AES trước khi lưu vào DB
                String encryptedPassword;
                try {
                    encryptedPassword = aesHelper.encrypt("123456");
                } catch (Exception e) {
                    throw new RuntimeException("Failed to encrypt password", e);
                }

                User admin = User.builder()
                        .username("admin")
                        .password(encryptedPassword) // Lưu AES encrypted password vào DB
                        .fullName("Administrator")
                        .email("admin@example.com")
                        .phoneNumber("0123456789")
                        .dob(LocalDateTime.of(1990, 1, 1, 0, 0))
                        .roles(Set.of(adminRole))
                        .failLogin(0L)
                        .gender(1L)
                        .isBlock(Constants.IS_BLOCK.FALSE) // set đúng để query match
                        .isFirstLogin(true)
                        .build();

                userRepository.save(admin);
            }
        };
    }
}
