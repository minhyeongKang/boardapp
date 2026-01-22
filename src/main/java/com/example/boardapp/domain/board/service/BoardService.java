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
    private final UserRepository userRepository;

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

    public List<BoardResponseDto> findByUserId(Long userId) {
        return boardRepository.findAllByUserIdDesc(userId);
    }

    // 게시글 단건 조회 (수정 화면에서 사용)
    public BoardResponseDto findOne(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return toResponse(board);
    }

    // 게시글 수정 (내 글만 가능)
    public void update(Long boardId, Long userId, BoardRequestDto req) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!board.getUserId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        board.setTitle(req.getTitle());
        board.setContent(req.getContent());

        int updated = boardRepository.update(board);
        if (updated == 0) {
            throw new RuntimeException("수정에 실패했습니다.");
        }
    }

    private BoardResponseDto toResponse(Board b) {
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