package com.example.boardapp.global.security;

import com.example.boardapp.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security가 인식할 수 있는 User 정보로 변환
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 기능은 추후 확장 가능 (지금은 미사용)
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // DB에 암호화 저장되어 있어야 함
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 상태: 현재 모두 true = 활성화 처리
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
