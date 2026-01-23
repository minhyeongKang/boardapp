package com.example.boardapp.domain.like.service;

import com.example.boardapp.domain.like.dto.LikeResponseDto;
import com.example.boardapp.domain.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public LikeResponseDto like(Long boardId, Long userId) {
        try {
            likeRepository.insert(boardId, userId);
        } catch (DataIntegrityViolationException ignore) {
        }

        long cnt = likeRepository.count(boardId);
        return new LikeResponseDto(boardId, cnt, true);
    }

    @Transactional
    public LikeResponseDto unlike(Long boardId, Long userId) {
        if (likeRepository.exists(boardId, userId)) {
            likeRepository.delete(boardId, userId);
        }
        long cnt = likeRepository.count(boardId);
        return new LikeResponseDto(boardId, cnt, false);
    }

    public LikeResponseDto status(Long boardId, Long userId) {
        boolean liked = likeRepository.exists(boardId, userId);
        long cnt = likeRepository.count(boardId);
        return new LikeResponseDto(boardId, cnt, liked);
    }
}
