package com.example.boardapp.global.config;

import com.example.boardapp.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ✅ API 서버: 세션 안 씀
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // ✅ 인증 실패/권한 없음 시 로그인 페이지로 리다이렉트하지 말고 401/403 그대로 반환
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )

                .authorizeHttpRequests(auth -> auth
                        // ✅ 프리플라이트 허용 (간혹 필요)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 정적 리소스/페이지 허용
                        .requestMatchers(
                                "/", "/index.html", "/signup.html", "/main.html", "/mypage.html", "/write.html", "/user.html", "/edit.html",
                                "/css/**", "/js/**", "/images/**", "/favicon.ico", "/error"
                        ).permitAll()

                        // ✅ 로그인/회원가입 API는 무조건 허용 (POST까지 명시)
                        .requestMatchers(HttpMethod.POST, "/api/users/login", "/api/users/signup").permitAll()

                        // ✅ 나머지는 인증 필요
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}