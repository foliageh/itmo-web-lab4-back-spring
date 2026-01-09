package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import({ShotRepository.class, ShotRowMapper.class, UserRowMapper.class})
@Sql(scripts = "/test-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShotRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ShotRowMapper shotRowMapper;

    @Autowired
    private UserRowMapper userRowMapper;

    private ShotRepository shotRepository;
    private User testUser;

    @BeforeEach
    void setUp() {
        shotRepository = new ShotRepository(jdbcTemplate, shotRowMapper);
        
        // Создаем тестового пользователя
        com.twillice.itmoweblab4backspring.repository.UserRepository userRepo = 
                new com.twillice.itmoweblab4backspring.repository.UserRepository(jdbcTemplate, userRowMapper);
        testUser = userRepo.save(User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build());
    }

    @Test
    void testSave_NewShot() {
        // Arrange
        Shot shot = Shot.builder()
                .x(1.5)
                .y(2.0)
                .r(3.0)
                .user(testUser)
                .build();

        // Act
        Shot saved = shotRepository.save(shot);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(1.5, saved.getX());
        assertEquals(2.0, saved.getY());
        assertEquals(3.0, saved.getR());
        assertNotNull(saved.getInArea()); // должно быть вычислено
        assertNotNull(saved.getShotTime()); // должно быть установлено
        assertEquals(testUser.getId(), saved.getUser().getId());
    }

    @Test
    void testFindByUser_Success() {
        // Arrange
        Shot shot1 = shotRepository.save(Shot.builder()
                .x(1.0)
                .y(1.0)
                .r(2.0)
                .user(testUser)
                .build());
        
        Shot shot2 = shotRepository.save(Shot.builder()
                .x(2.0)
                .y(2.0)
                .r(3.0)
                .user(testUser)
                .build());

        // Act
        List<Shot> shots = shotRepository.findByUser(testUser);

        // Assert
        assertNotNull(shots);
        assertTrue(shots.size() >= 2);
        assertTrue(shots.stream().anyMatch(s -> s.getId().equals(shot1.getId())));
        assertTrue(shots.stream().anyMatch(s -> s.getId().equals(shot2.getId())));
    }

    @Test
    void testFindByUser_EmptyList() {
        // Arrange - создаем другого пользователя
        com.twillice.itmoweblab4backspring.repository.UserRepository userRepo = 
                new com.twillice.itmoweblab4backspring.repository.UserRepository(jdbcTemplate, userRowMapper);
        User otherUser = userRepo.save(User.builder()
                .username("otheruser")
                .password("password")
                .roles("USER")
                .build());

        // Act
        List<Shot> shots = shotRepository.findByUser(otherUser);

        // Assert
        assertNotNull(shots);
        assertTrue(shots.isEmpty());
    }

    @Test
    void testSave_CalculatesInArea() {
        // Arrange - точка внутри области
        Shot shotInArea = Shot.builder()
                .x(-1.0)
                .y(-1.0)
                .r(3.0)
                .user(testUser)
                .build();

        // Act
        Shot saved = shotRepository.save(shotInArea);

        // Assert
        assertTrue(saved.getInArea());
    }

    @Test
    void testSave_CalculatesInArea_Outside() {
        // Arrange - точка вне области
        Shot shotOutside = Shot.builder()
                .x(10.0)
                .y(10.0)
                .r(3.0)
                .user(testUser)
                .build();

        // Act
        Shot saved = shotRepository.save(shotOutside);

        // Assert
        assertFalse(saved.getInArea());
    }
}
