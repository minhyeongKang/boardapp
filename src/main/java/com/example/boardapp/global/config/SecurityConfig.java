package com.example.boardapp.global.config;

import com.example.boardapp.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())  // CSR 보호 비활성화 — API 서버이므로
                .formLogin(form -> form.disable()) // 기본 로그인 폼 사용 안 함
                .httpBasic(basic -> basic.disable()) // Basic Auth 사용 안 함
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/login", "/users/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
