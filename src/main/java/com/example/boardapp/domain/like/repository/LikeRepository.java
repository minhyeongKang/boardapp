package com.example.boardapp.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final JdbcTemplate jdbc;

    public boolean exists(Long boardId, Long userId) {
        String sql = "SELECT COUNT(*) FROM BOARD_LIKES WHERE BOARD_ID=? AND USER_ID=?";
        Integer cnt = jdbc.queryForObject(sql, Integer.class, boardId, userId);
        return cnt != null && cnt > 0;
    }

    public void insert(Long boardId, Long userId) {
        String sql = "INSERT INTO BOARD_LIKES (BOARD_ID, USER_ID) VALUES (?, ?)";
        jdbc.update(sql, boardId, userId);
    }

    public void delete(Long boardId, Long userId) {
        String sql = "DELETE FROM BOARD_LIKES WHERE BOARD_ID=? AND USER_ID=?";
        jdbc.update(sql, boardId, userId);
    }

    public long count(Long boardId) {
        String sql = "SELECT COUNT(*) FROM BOARD_LIKES WHERE BOARD_ID=?";
        Long cnt = jdbc.queryForObject(sql, Long.class, boardId);
        return cnt == null ? 0L : cnt;
    }
}
