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
                .csrf(csrf -> csrf.disable())  // API 서버이므로 CSRF 보호 비활성화
                .formLogin(form -> form.disable()) // 기본 로그인 폼 사용 안 함
                .httpBasic(basic -> basic.disable()) // Basic Auth 사용 안 함
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스 및 루트 페이지 허용
                        .requestMatchers(
                                "/", "/index.html", "/signup.html", "/main.html", "/mypage.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico",

                                // 인증 없이 허용할 API
                                "/api/users/login", "/api/users/signup"
                        ).permitAll()
                        // 그 외 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}