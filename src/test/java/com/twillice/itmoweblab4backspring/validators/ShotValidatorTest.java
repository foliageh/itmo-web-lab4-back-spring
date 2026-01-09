package com.twillice.itmoweblab4backspring.validators;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShotValidatorTest {

    @InjectMocks
    private ShotValidator validator;

    @Test
    void testValidate_Success() {
        // Arrange
        ShotCreateRequest validRequest = new ShotCreateRequest(1.5, 2.0, 3.0);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(validRequest));
    }

    @Test
    void testValidate_NullX() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(null, 2.0, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("X must not be null"));
    }

    @Test
    void testValidate_NullY() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, null, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Y must not be null"));
    }

    @Test
    void testValidate_NullR() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 2.0, null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("R must not be null"));
    }

    @Test
    void testValidate_XOutOfRange_TooSmall() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(-6.0, 2.0, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("X must be between"));
    }

    @Test
    void testValidate_XOutOfRange_TooLarge() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(6.0, 2.0, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("X must be between"));
    }

    @Test
    void testValidate_YOutOfRange_TooSmall() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, -6.0, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Y must be between"));
    }

    @Test
    void testValidate_YOutOfRange_TooLarge() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 6.0, 3.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Y must be between"));
    }

    @Test
    void testValidate_ROutOfRange_TooSmall() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 2.0, 0.05);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("R must be between"));
    }

    @Test
    void testValidate_ROutOfRange_TooLarge() {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 2.0, 6.0);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("R must be between"));
    }

    @Test
    void testValidate_BoundaryValues() {
        // Arrange - граничные значения
        ShotCreateRequest request1 = new ShotCreateRequest(-5.0, -5.0, 0.1);
        ShotCreateRequest request2 = new ShotCreateRequest(5.0, 5.0, 5.0);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(request1));
        assertDoesNotThrow(() -> validator.validate(request2));
    }
}
