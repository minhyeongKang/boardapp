package com.example.boardapp.domain.like.controller;

import com.example.boardapp.domain.like.dto.LikeResponseDto;
import com.example.boardapp.domain.like.service.LikeService;
import com.example.boardapp.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardId}/likes")
    public ResponseEntity<LikeResponseDto> like(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(likeService.like(boardId, userId));
    }

    @DeleteMapping("/{boardId}/likes")
    public ResponseEntity<LikeResponseDto> unlike(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(likeService.unlike(boardId, userId));
    }

    @GetMapping("/{boardId}/likes")
    public ResponseEntity<LikeResponseDto> status(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(likeService.status(boardId, userId));
    }
}
