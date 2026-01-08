package com.example.boardapp.domain.board.entity;

import com.example.boardapp.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board extends BaseTimeEntity {
    private Long id;
    private Long userId;
    private String title;
    private String content;
}