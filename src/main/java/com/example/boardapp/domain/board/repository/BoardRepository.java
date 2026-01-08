package com.example.boardapp.domain.board.repository;

import com.example.boardapp.domain.board.dto.BoardResponseDto;
import com.example.boardapp.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final JdbcTemplate jdbc;

    public void save(Board board) {
        String sql = """
            INSERT INTO BOARDS (USER_ID, TITLE, CONTENT)
            VALUES (?, ?, ?)
        """;
        jdbc.update(sql, board.getUserId(), board.getTitle(), board.getContent());
    }

    public Optional<Board> findLatestByUserId(Long userId) {
        String sql = """
            SELECT *
            FROM BOARDS
            WHERE USER_ID = ?
            ORDER BY ID DESC
            FETCH FIRST 1 ROWS ONLY
        """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                Board b = new Board();
                b.setId(rs.getLong("ID"));
                b.setUserId(rs.getLong("USER_ID"));
                b.setTitle(rs.getString("TITLE"));
                b.setContent(rs.getString("CONTENT"));

                Timestamp created = rs.getTimestamp("CREATEDAT");
                Timestamp modified = rs.getTimestamp("MODIFIEDAT");
                if (created != null) b.setCreatedAt(created.toLocalDateTime());
                if (modified != null) b.setModifiedAt(modified.toLocalDateTime());

                return Optional.of(b);
            }
            return Optional.empty();
        }, userId);
    }

    public List<Board> findAllDesc() {
        String sql = """
            SELECT b.*
            FROM BOARDS b
            ORDER BY b.ID DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Board b = new Board();
            b.setId(rs.getLong("ID"));
            b.setUserId(rs.getLong("USER_ID"));
            b.setTitle(rs.getString("TITLE"));
            b.setContent(rs.getString("CONTENT"));

            Timestamp created = rs.getTimestamp("CREATEDAT");
            Timestamp modified = rs.getTimestamp("MODIFIEDAT");
            if (created != null) b.setCreatedAt(created.toLocalDateTime());
            if (modified != null) b.setModifiedAt(modified.toLocalDateTime());

            return b;
        });
    }

    // 내 글 목록
    public List<Board> findByUserIdDesc(Long userId) {
        String sql = """
            SELECT *
            FROM BOARDS
            WHERE USER_ID = ?
            ORDER BY ID DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Board b = new Board();
            b.setId(rs.getLong("ID"));
            b.setUserId(rs.getLong("USER_ID"));
            b.setTitle(rs.getString("TITLE"));
            b.setContent(rs.getString("CONTENT"));

            Timestamp created = rs.getTimestamp("CREATEDAT");
            Timestamp modified = rs.getTimestamp("MODIFIEDAT");
            if (created != null) b.setCreatedAt(created.toLocalDateTime());
            if (modified != null) b.setModifiedAt(modified.toLocalDateTime());

            return b;
        }, userId);
    }

    public List<BoardResponseDto> findAllByUserIdDesc(Long userId) {
        String sql = """
        SELECT
            b.ID,
            b.USER_ID,
            u.NICKNAME AS NICKNAME,
            b.TITLE,
            b.CONTENT,
            b.CREATEDAT,
            b.MODIFIEDAT
        FROM BOARDS b
        JOIN USERS u ON u.ID = b.USER_ID
        WHERE b.USER_ID = ?
        ORDER BY b.ID DESC
    """;

        return jdbc.query(sql, (rs, rowNum) -> {
            Timestamp created = rs.getTimestamp("CREATEDAT");
            Timestamp modified = rs.getTimestamp("MODIFIEDAT");

            return new BoardResponseDto(
                    rs.getLong("ID"),
                    rs.getLong("USER_ID"),
                    rs.getString("NICKNAME"),
                    rs.getString("TITLE"),
                    rs.getString("CONTENT"),
                    created != null ? created.toLocalDateTime() : null,
                    modified != null ? modified.toLocalDateTime() : null
            );
        }, userId);
    }
}