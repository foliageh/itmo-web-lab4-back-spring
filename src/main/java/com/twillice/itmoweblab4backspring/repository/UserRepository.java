package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    private static final String FIND_BY_USERNAME_SQL = 
        "SELECT id, username, password, roles FROM \"_User\" WHERE username = :username";
    
    private static final String EXISTS_BY_USERNAME_SQL = 
        "SELECT COUNT(*) > 0 FROM \"_User\" WHERE username = :username";
    
    private static final String INSERT_SQL = 
        "INSERT INTO \"_User\" (username, password, roles) VALUES (:username, :password, :roles)";
    
    private static final String FIND_BY_ID_SQL = 
        "SELECT id, username, password, roles FROM \"_User\" WHERE id = :id";

    public Optional<User> findByUsername(String username) {
        var params = new MapSqlParameterSource("username", username);
        var users = jdbcTemplate.query(FIND_BY_USERNAME_SQL, params, userRowMapper);
        return users.stream().findFirst();
    }

    public boolean existsByUsername(String username) {
        var params = new MapSqlParameterSource("username", username);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXISTS_BY_USERNAME_SQL, params, Boolean.class));
    }

    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    private User insert(User user) {
        var params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword())
                .addValue("roles", user.getRoles() != null ? user.getRoles() : "USER");
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL, params, keyHolder, new String[]{"id"});
        
        Long id = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        return user.toBuilder().id(id).build();
    }

    private User update(User user) {
        var params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword())
                .addValue("roles", user.getRoles());
        
        jdbcTemplate.update(
            "UPDATE \"_User\" SET username = :username, password = :password, roles = :roles WHERE id = :id",
            params
        );
        return user;
    }

    public Optional<User> findById(Long id) {
        var params = new MapSqlParameterSource("id", id);
        var users = jdbcTemplate.query(FIND_BY_ID_SQL, params, userRowMapper);
        return users.stream().findFirst();
    }
}
