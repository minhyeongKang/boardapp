package com.example.boardapp.domain.user.entity;

import com.example.boardapp.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseTimeEntity {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String intro;
}
