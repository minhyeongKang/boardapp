package com.example.boardapp.domain.comment.controller;

import com.example.boardapp.domain.comment.dto.CommentRequestDto;
import com.example.boardapp.domain.comment.dto.CommentResponseDto;
import com.example.boardapp.domain.comment.service.CommentService;
import com.example.boardapp.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 특정 게시글의 댓글 목록
    @GetMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<List<CommentResponseDto>> list(@PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.findByBoardId(boardId));
    }

    // 특정 게시글에 댓글 작성 (로그인 필요)
    @PostMapping("/api/boards/{boardId}/comments")
    public ResponseEntity<Void> create(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto req
    ) {
        Long userId = userDetails.getUser().getId();
        commentService.create(boardId, userId, req);
        return ResponseEntity.ok().build();
    }

    // 댓글 수정 (본인만)
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> update(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto req
    ) {
        Long userId = userDetails.getUser().getId();
        commentService.update(commentId, userId, req);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제 (본인만)
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        commentService.delete(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
