package com.example.boardapp.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private Long boardId;
    private Long likeCount;
    private Boolean likedByMe;
}
