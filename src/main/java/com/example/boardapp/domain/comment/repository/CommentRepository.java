package com.example.boardapp.domain.comment.repository;

import com.example.boardapp.domain.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final JdbcTemplate jdbc;

    public void save(Long boardId, Long userId, String content) {
        String sql = """
            INSERT INTO COMMENTS (ID, BOARD_ID, USER_ID, CONTENT, CREATEDAT, MODIFIEDAT)
            VALUES (COMMENTS_SEQ.NEXTVAL, ?, ?, ?, ?, ?)
        """;
        LocalDateTime now = LocalDateTime.now();
        jdbc.update(sql, boardId, userId, content, Timestamp.valueOf(now), Timestamp.valueOf(now));
    }

    public List<CommentResponseDto> findByBoardId(Long boardId) {
        String sql = """
            SELECT
              c.ID,
              c.BOARD_ID,
              c.USER_ID,
              u.NICKNAME,
              c.CONTENT,
              c.CREATEDAT,
              c.MODIFIEDAT
            FROM COMMENTS c
            JOIN USERS u ON c.USER_ID = u.ID
            WHERE c.BOARD_ID = ?
            ORDER BY c.ID ASC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Timestamp created = rs.getTimestamp("CREATEDAT");
            Timestamp modified = rs.getTimestamp("MODIFIEDAT");

            return new CommentResponseDto(
                    rs.getLong("ID"),
                    rs.getLong("BOARD_ID"),
                    rs.getLong("USER_ID"),
                    rs.getString("NICKNAME"),
                    rs.getString("CONTENT"),
                    created != null ? created.toLocalDateTime() : null,
                    modified != null ? modified.toLocalDateTime() : null
            );
        }, boardId);
    }

    public int update(Long commentId, Long userId, String content) {
        String sql = """
            UPDATE COMMENTS
            SET CONTENT = ?, MODIFIEDAT = ?
            WHERE ID = ? AND USER_ID = ?
        """;
        return jdbc.update(sql, content, Timestamp.valueOf(LocalDateTime.now()), commentId, userId);
    }

    public int delete(Long commentId, Long userId) {
        // 내 댓글만 삭제되도록 ID + USER_ID 둘 다 조건으로 건다
        String sql = "DELETE FROM COMMENTS WHERE ID = ? AND USER_ID = ?";
        return jdbc.update(sql, commentId, userId);
    }
}

