package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import({UserRepository.class, UserRowMapper.class})
@Sql(scripts = "/test-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UserRowMapper userRowMapper;

    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(jdbcTemplate, userRowMapper);
        testUser = User.builder()
                .username("testuser")
                .password("encoded_password")
                .roles("USER")
                .build();
    }

    @Test
    void testSave_NewUser() {
        // Act
        User saved = userRepository.save(testUser);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("testuser", saved.getUsername());
        assertEquals("encoded_password", saved.getPassword());
        assertEquals("USER", saved.getRoles());
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        User saved = userRepository.save(testUser);

        // Act
        Optional<User> found = userRepository.findByUsername("testuser");

        // Assert
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        // Act
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByUsername_True() {
        // Arrange
        userRepository.save(testUser);

        // Act
        boolean exists = userRepository.existsByUsername("testuser");

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByUsername_False() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }

    @Test
    void testSave_UpdateExisting() {
        // Arrange
        User saved = userRepository.save(testUser);
        User updated = saved.toBuilder()
                .password("new_password")
                .roles("ADMIN")
                .build();

        // Act
        User result = userRepository.save(updated);

        // Assert
        assertEquals(saved.getId(), result.getId());
        assertEquals("new_password", result.getPassword());
        assertEquals("ADMIN", result.getRoles());
    }
}
