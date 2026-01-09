package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShotRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ShotRowMapper shotRowMapper;

    private static final String FIND_BY_USER_SQL = 
        "SELECT s.id as shot_id, s.x, s.y, s.r, s.in_area, s.shot_time, s.user_id, " +
        "u.id, u.username, u.password, u.roles " +
        "FROM Shot s " +
        "JOIN \"_User\" u ON s.user_id = u.id " +
        "WHERE s.user_id = :userId " +
        "ORDER BY s.id DESC";
    
    private static final String INSERT_SQL = 
        "INSERT INTO Shot (x, y, r, in_area, shot_time, user_id) " +
        "VALUES (:x, :y, :r, :inArea, :shotTime, :userId)";

    public List<Shot> findByUser(User user) {
        var params = new MapSqlParameterSource("userId", user.getId());
        return jdbcTemplate.query(FIND_BY_USER_SQL, params, shotRowMapper);
    }

    public Shot save(Shot shot) {
        if (shot.getId() == null) {
            return insert(shot);
        } else {
            return update(shot);
        }
    }

    private Shot insert(Shot shot) {
        Boolean inArea = shot.getInArea();
        if (inArea == null) {
            inArea = checkHit(shot.getX(), shot.getY(), shot.getR());
        }
        
        String shotTime = shot.getShotTime();
        if (shotTime == null) {
            shotTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }

        var params = new MapSqlParameterSource()
                .addValue("x", shot.getX())
                .addValue("y", shot.getY())
                .addValue("r", shot.getR())
                .addValue("inArea", inArea)
                .addValue("shotTime", shotTime)
                .addValue("userId", shot.getUser().getId());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SQL, params, keyHolder, new String[]{"id"});
        
        Long id = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        return shot.toBuilder()
                .id(id)
                .inArea(inArea)
                .shotTime(shotTime)
                .build();
    }

    private Shot update(Shot shot) {
        var params = new MapSqlParameterSource()
                .addValue("id", shot.getId())
                .addValue("x", shot.getX())
                .addValue("y", shot.getY())
                .addValue("r", shot.getR())
                .addValue("inArea", shot.getInArea())
                .addValue("shotTime", shot.getShotTime())
                .addValue("userId", shot.getUser().getId());
        
        jdbcTemplate.update(
            "UPDATE Shot SET x = :x, y = :y, r = :r, in_area = :inArea, shot_time = :shotTime, user_id = :userId WHERE id = :id",
            params
        );
        return shot;
    }

    private Boolean checkHit(Double x, Double y, Double r) {
        boolean area1_hit = x <= 0 && y <= 0 && x >= -r && y >= -r / 2;
        boolean area2_hit = x <= 0 && y >= 0 && y <= 2 * (x + r / 2);
        boolean area3_hit = x >= 0 && y >= 0 && x * x + y * y <= (r / 2) * (r / 2);
        return area1_hit || area2_hit || area3_hit;
    }
}
