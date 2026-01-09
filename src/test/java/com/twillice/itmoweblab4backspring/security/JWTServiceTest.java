package com.twillice.itmoweblab4backspring.security;

import com.twillice.itmoweblab4backspring.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JWTService jwtService;

    private User testUser;
    private String testSigningKey;

    @BeforeEach
    void setUp() {
        // Генерируем тестовый ключ (256 бит в Base64)
        testSigningKey = Base64.getEncoder().encodeToString(
                "test_secret_key_for_jwt_testing_purposes_only_123456".getBytes()
        );

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        when(jwtConfig.getJwtSigningKey()).thenReturn(testSigningKey);
    }

    @Test
    void testGenerateToken_Success() {
        // Act
        String token = jwtService.generateToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT состоит из 3 частей
    }

    @Test
    void testExtractUsername_Success() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertNotNull(username);
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid_Success() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act
        boolean isValid = jwtService.isTokenValid(token, testUser);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_WrongUser() {
        // Arrange
        String token = jwtService.generateToken(testUser);
        User differentUser = User.builder()
                .id(2L)
                .username("differentuser")
                .password("password")
                .roles("USER")
                .build();

        // Act
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testTokenContainsUserInfo() {
        // Arrange
        String token = jwtService.generateToken(testUser);

        // Act - декодируем токен для проверки содержимого
        byte[] keyBytes = Base64.getDecoder().decode(testSigningKey);
        javax.crypto.SecretKey secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
        
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Assert
        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.get("id"));
        assertNotNull(claims.get("roles"));
    }
}
