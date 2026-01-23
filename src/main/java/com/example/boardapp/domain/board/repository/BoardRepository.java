package com.example.boardapp.domain.board.repository;

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
                return Optional.of(mapBoard(rs));
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

        return jdbc.query(sql, (rs, rowNum) -> mapBoard(rs));
    }

    public List<Board> findByUserIdDesc(Long userId) {
        String sql = """
            SELECT *
            FROM BOARDS
            WHERE USER_ID = ?
            ORDER BY ID DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> mapBoard(rs), userId);
    }

    public Optional<Board> findById(Long boardId) {
        String sql = """
            SELECT *
            FROM BOARDS
            WHERE ID = ?
        """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(mapBoard(rs));
            }
            return Optional.empty();
        }, boardId);
    }

    public int update(Board board) {
        String sql = """
            UPDATE BOARDS
            SET TITLE = ?, CONTENT = ?, MODIFIEDAT = SYSDATE
            WHERE ID = ?
        """;
        return jdbc.update(sql, board.getTitle(), board.getContent(), board.getId());
    }

    public int deleteById(Long boardId) {
        String sql = "DELETE FROM BOARDS WHERE ID = ?";
        return jdbc.update(sql, boardId);
    }

    private Board mapBoard(java.sql.ResultSet rs) throws java.sql.SQLException {
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
    }
}