package com.example.boardapp.domain.user.controller;

import com.example.boardapp.domain.user.dto.LoginRequestDto;
import com.example.boardapp.domain.user.dto.UserMeResponseDto;
import com.example.boardapp.domain.user.entity.User;
import com.example.boardapp.domain.user.service.UserService;
import com.example.boardapp.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // Map 말고 이미 만든 LoginRequestDto 사용(기능 유지 + 안전)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return ResponseEntity.ok("로그아웃 성공 (클라이언트 토큰 삭제 필요)");
    }

    // SecurityContext에서 String 캐스팅하지 말고 principal(UserDetailsImpl)에서 꺼내기
    @PutMapping("/me")
    public ResponseEntity<String> updateMe(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, String> request
    ) {
        String email = userDetails.getUser().getEmail();

        String nickname = request.getOrDefault("nickname", "");
        String intro = request.getOrDefault("intro", "");

        userService.updateMyProfile(email, nickname, intro);
        return ResponseEntity.ok("수정 완료");
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponseDto> me(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(UserMeResponseDto.from(userDetails.getUser()));
    }
}