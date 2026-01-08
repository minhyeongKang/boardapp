package com.example.boardapp.domain.board.controller;

import com.example.boardapp.domain.board.dto.BoardRequestDto;
import com.example.boardapp.domain.board.dto.BoardResponseDto;
import com.example.boardapp.domain.board.service.BoardService;
import com.example.boardapp.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BoardRequestDto req
    ) {
        Long userId = userDetails.getUser().getId();
        boardService.create(userId, req);
        return ResponseEntity.ok().build();
    }

    // 전체 피드 (DTO로)
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> list() {
        return ResponseEntity.ok(boardService.findAll());
    }

    // 내 글
    @GetMapping("/mine")
    public ResponseEntity<List<BoardResponseDto>> mine(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(boardService.findMine(userId));
    }

    @GetMapping("/user/id/{userId}")
    public ResponseEntity<List<BoardResponseDto>> postsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(boardService.findByUserId(userId));
    }
}