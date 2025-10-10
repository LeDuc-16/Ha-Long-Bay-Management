package com.example.file_srv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration for File Service
 * 
 * File-srv không tự validate JWT vì đã được API Gateway xác thực.
 * Gateway sẽ forward user info qua headers: X-User-Id, X-User-Scope
 * File-srv chỉ cần tin tưởng các headers này.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cho phép tất cả requests vì gateway đã xác thực JWT
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                // Tắt CSRF vì đây là REST API
                .csrf(AbstractHttpConfigurer::disable)
                // Tắt form login
                .formLogin(AbstractHttpConfigurer::disable)
                // Tắt HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
