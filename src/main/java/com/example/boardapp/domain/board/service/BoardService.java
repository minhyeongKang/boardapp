package com.example.boardapp.domain.board.service;

import com.example.boardapp.domain.board.dto.BoardRequestDto;
import com.example.boardapp.domain.board.dto.BoardResponseDto;
import com.example.boardapp.domain.board.entity.Board;
import com.example.boardapp.domain.board.repository.BoardRepository;
import com.example.boardapp.domain.user.entity.User;
import com.example.boardapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository; // 닉네임 조회용 (이미 프로젝트에 있음)

    public void create(Long userId, BoardRequestDto req) {
        Board b = new Board();
        b.setUserId(userId);
        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
        boardRepository.save(b);
    }

    public List<BoardResponseDto> findAll() {
        List<Board> boards = boardRepository.findAllDesc();
        return boards.stream().map(this::toResponse).toList();
    }

    public List<BoardResponseDto> findMine(Long userId) {
        List<Board> boards = boardRepository.findByUserIdDesc(userId);
        return boards.stream().map(this::toResponse).toList();
    }

    private BoardResponseDto toResponse(Board b) {
        // 닉네임 표시 위해 user 조회
        User user = userRepository.findById(b.getUserId()).orElse(null);
        String nickname = (user != null ? user.getNickname() : "USER#" + b.getUserId());

        return new BoardResponseDto(
                b.getId(),
                b.getUserId(),
                nickname,
                b.getTitle(),
                b.getContent(),
                b.getCreatedAt(),
                b.getModifiedAt()
        );
    }
}