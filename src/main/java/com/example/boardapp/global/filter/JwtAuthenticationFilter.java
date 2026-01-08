package com.example.boardapp.global.filter;

import com.example.boardapp.global.util.JwtUtil;
import com.example.boardapp.global.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 요청 헤더에서 JWT 토큰을 확인하고 인증 처리하는 필터
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // ✅ 추가

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {

                // ✅ 토큰 subject는 이제 username임
                String username = jwtUtil.extractUsername(token);

                // ✅ UserDetails 로드해서 principal에 넣기
                var userDetails = userDetailsService.loadUserByUsername(username);

                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,              // principal
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();

        // ✅ 정적 리소스/페이지는 제외
        if (uri.equals("/") ||
                uri.equals("/index.html") ||
                uri.equals("/signup.html") ||
                uri.equals("/main.html") ||
                uri.equals("/mypage.html") ||
                uri.equals("/write.html") ||
                uri.startsWith("/css/") ||
                uri.startsWith("/js/") ||
                uri.startsWith("/images/") ||
                uri.equals("/favicon.ico") ||
                uri.equals("/error")) {
            return true;
        }

        // ✅ 로그인/회원가입은 JWT 검사 제외
        return uri.equals("/api/users/login") || uri.equals("/api/users/signup");
    }
}