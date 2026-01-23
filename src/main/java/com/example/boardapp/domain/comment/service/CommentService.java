package com.example.boardapp.domain.comment.service;

import com.example.boardapp.domain.comment.dto.CommentRequestDto;
import com.example.boardapp.domain.comment.dto.CommentResponseDto;
import com.example.boardapp.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Long boardId, Long userId, CommentRequestDto req) {
        String content = (req.getContent() == null) ? "" : req.getContent().trim();
        if (content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }
        commentRepository.save(boardId, userId, content);
    }

    public List<CommentResponseDto> findByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public void update(Long commentId, Long userId, CommentRequestDto req) {
        String content = (req.getContent() == null) ? "" : req.getContent().trim();
        if (content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }
        int updated = commentRepository.update(commentId, userId, content);
        if (updated == 0) {
            throw new IllegalArgumentException("수정 권한이 없거나 댓글이 없습니다.");
        }
    }

    public void delete(Long commentId, Long userId) {
        // Repository에서 실제 삭제 실행
        int deleted = commentRepository.delete(commentId, userId);

        // deleted == 0 이면
        // 1) 댓글이 없거나
        // 2) 내 댓글이 아니라서 조건(ID, USER_ID)에 안 맞아서 삭제가 안 된 것
        if (deleted == 0) {
            throw new IllegalArgumentException("삭제 권한이 없거나 댓글이 없습니다.");
        }
    }
}
