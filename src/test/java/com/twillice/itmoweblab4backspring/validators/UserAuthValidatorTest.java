package com.twillice.itmoweblab4backspring.validators;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthValidator validator;

    private UserAuthRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserAuthRequest("testuser", "password123");
    }

    @Test
    void testValidateRegistration_Success() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateRegistration(validRequest));
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void testValidateRegistration_UserAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validateRegistration(validRequest)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("already exists"));
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void testValidateLogin_Success() {
        // Act & Assert
        assertDoesNotThrow(() -> validator.validateLogin(validRequest));
        verify(userRepository, never()).existsByUsername(anyString());
    }
}
