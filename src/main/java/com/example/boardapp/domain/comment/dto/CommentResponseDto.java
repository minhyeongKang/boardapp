package com.example.boardapp.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private String nickname;      // users 테이블에서 join해서 가져올 값
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

