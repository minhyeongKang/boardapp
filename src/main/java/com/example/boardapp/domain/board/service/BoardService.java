package com.example.boardapp.domain.board.service;

import com.example.boardapp.domain.board.dto.BoardRequestDto;
import com.example.boardapp.domain.board.dto.BoardResponseDto;
import com.example.boardapp.domain.board.entity.Board;
import com.example.boardapp.domain.board.repository.BoardRepository;
import com.example.boardapp.domain.like.repository.LikeRepository;
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
    private final LikeRepository likeRepository;

    public void create(Long userId, BoardRequestDto req) {
        Board b = new Board();
        b.setUserId(userId);
        b.setTitle(req.getTitle());
        b.setContent(req.getContent());
        boardRepository.save(b);
    }

    public List<BoardResponseDto> findAll(Long viewerUserId) {
        return boardRepository.findAllDesc().stream()
                .map(b -> toResponse(b, viewerUserId))
                .toList();
    }

    public List<BoardResponseDto> findMine(Long ownerUserId, Long viewerUserId) {
        return boardRepository.findByUserIdDesc(ownerUserId).stream()
                .map(b -> toResponse(b, viewerUserId))
                .toList();
    }

    public List<BoardResponseDto> findByUserId(Long ownerUserId, Long viewerUserId) {
        return boardRepository.findByUserIdDesc(ownerUserId).stream()
                .map(b -> toResponse(b, viewerUserId))
                .toList();
    }

    public BoardResponseDto findOne(Long boardId, Long viewerUserId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return toResponse(board, viewerUserId);
    }

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

    public void delete(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!board.getUserId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        int deleted = boardRepository.deleteById(boardId);
        if (deleted == 0) {
            throw new RuntimeException("삭제에 실패했습니다.");
        }
    }

    private BoardResponseDto toResponse(Board b, Long viewerUserId) {
        User user = userRepository.findById(b.getUserId()).orElse(null);
        String nickname = (user != null ? user.getNickname() : "USER#" + b.getUserId());

        long likeCount = likeRepository.count(b.getId());
        boolean likedByMe = viewerUserId != null && likeRepository.exists(b.getId(), viewerUserId);

        return new BoardResponseDto(
                b.getId(),
                b.getUserId(),
                nickname,
                b.getTitle(),
                b.getContent(),
                b.getCreatedAt(),
                b.getModifiedAt(),
                likeCount,
                likedByMe
        );
    }

}