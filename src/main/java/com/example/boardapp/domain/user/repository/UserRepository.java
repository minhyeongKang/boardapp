package com.example.boardapp.domain.user.repository;

import com.example.boardapp.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final JdbcTemplate jdbc;

    public void save(User user) {
        String sql = """
            INSERT INTO users (username, password, nickname, email, intro)
            VALUES (?, ?, ?, ?, ?)
        """;
        jdbc.update(sql, user.getUsername(), user.getPassword(), user.getNickname(),
                user.getEmail(), user.getIntro());
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setModifiedAt(rs.getTimestamp("modifiedAt").toLocalDateTime());
                return Optional.of(user);
            }
            return Optional.empty();
        }, username);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setIntro(rs.getString("intro"));
                user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                user.setModifiedAt(rs.getTimestamp("modifiedAt").toLocalDateTime());
                return Optional.of(user);
            }
            return Optional.empty();
        }, email);
    }

    public int updateProfileByEmail(String email, String nickname, String intro) {
        String sql = """
        UPDATE users
        SET nickname = ?, intro = ?, modifiedAt = SYSDATE
        WHERE email = ?
    """;
        return jdbc.update(sql, nickname, intro, email);
    }
}
