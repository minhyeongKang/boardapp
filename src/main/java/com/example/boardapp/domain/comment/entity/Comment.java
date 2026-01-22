package com.example.boardapp.domain.comment.entity;

import com.example.boardapp.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment extends BaseTimeEntity {
    private Long id;
    private Long boardId;
    private Long userId;
    private String content;
}
