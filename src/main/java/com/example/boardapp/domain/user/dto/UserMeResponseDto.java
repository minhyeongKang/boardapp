package com.example.boardapp.domain.user.dto;

import com.example.boardapp.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMeResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String intro;
    private String email;

    public static UserMeResponseDto from(User user) {
        return new UserMeResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getIntro(),
                user.getEmail()
        );
    }
}