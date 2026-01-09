package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShotRowMapper implements RowMapper<Shot> {
    @Override
    public Shot mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .roles(rs.getString("roles"))
                .build();
        
        return Shot.builder()
                .id(rs.getLong("shot_id"))
                .x(rs.getDouble("x"))
                .y(rs.getDouble("y"))
                .r(rs.getDouble("r"))
                .inArea(rs.getBoolean("in_area"))
                .shotTime(rs.getString("shot_time"))
                .user(user)
                .build();
    }
}
