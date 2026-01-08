package com.example.boardapp.domain.user.service;

import com.example.boardapp.domain.user.dto.UserResponseDto;
import com.example.boardapp.domain.user.entity.User;
import com.example.boardapp.domain.user.repository.UserRepository;
import com.example.boardapp.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void signup(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> { throw new RuntimeException("이미 존재하는 사용자명입니다."); });

        userRepository.findByEmail(user.getEmail())
                .ifPresent(u -> { throw new RuntimeException("이미 존재하는 이메일입니다."); });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰에는 email 말고 username을 넣어야 Security(UserDetailsServiceImpl)와 일관됨
        return jwtUtil.generateToken(user.getUsername());
    }

    public void logout() {
        // JWT는 상태가 없기 때문에 서버가 따로 처리할 것은 없음.
        // 클라이언트가 token 삭제하면 로그아웃.
    }

    public User getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setPassword(null);
        return user;
    }

    public void updateMyProfile(String email, String nickname, String intro) {
        int updated = userRepository.updateProfileByEmail(email, nickname, intro);
        if (updated == 0) {
            throw new RuntimeException("수정할 사용자를 찾을 수 없습니다.");
        }
    }

    public UserResponseDto me(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getIntro()
        );
    }
}