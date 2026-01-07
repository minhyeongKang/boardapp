package com.example.boardapp.domain.user.controller;

import com.example.boardapp.domain.user.entity.User;
import com.example.boardapp.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        userService.signup(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String token = userService.login(request.get("email"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return ResponseEntity.ok("로그아웃 성공 (클라이언트 토큰 삭제 필요)");
    }

    @GetMapping("/me")
    public ResponseEntity<User> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.getMyInfo(email));
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateMe(@RequestBody Map<String, String> request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();

        String nickname = request.getOrDefault("nickname", "");
        String intro = request.getOrDefault("intro", "");

        userService.updateMyProfile(email, nickname, intro);
        return ResponseEntity.ok("수정 완료");
    }
}
