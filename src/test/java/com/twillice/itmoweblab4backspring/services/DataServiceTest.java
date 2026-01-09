package com.twillice.itmoweblab4backspring.services;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.repository.ShotRepository;
import com.twillice.itmoweblab4backspring.validators.ShotValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    @Mock
    private ShotRepository shotRepository;

    @Mock
    private ShotValidator shotValidator;

    @InjectMocks
    private DataService dataService;

    private User testUser;
    private Shot testShot;
    private ShotCreateRequest validRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        testShot = Shot.builder()
                .id(1L)
                .x(1.5)
                .y(2.0)
                .r(3.0)
                .inArea(true)
                .shotTime("12:30:45")
                .user(testUser)
                .build();

        validRequest = new ShotCreateRequest(1.5, 2.0, 3.0);
    }

    @Test
    void testGetAllShotsByUser_Success() {
        // Arrange
        List<Shot> expectedShots = Arrays.asList(testShot);
        when(shotRepository.findByUser(testUser)).thenReturn(expectedShots);

        // Act
        List<Shot> result = dataService.getAllShotsByUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testShot, result.get(0));
        verify(shotRepository).findByUser(testUser);
    }

    @Test
    void testGetAllShotsByUser_EmptyList() {
        // Arrange
        when(shotRepository.findByUser(testUser)).thenReturn(Arrays.asList());

        // Act
        List<Shot> result = dataService.getAllShotsByUser(testUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shotRepository).findByUser(testUser);
    }

    @Test
    void testCreateShot_Success() {
        // Arrange
        doNothing().when(shotValidator).validate(any(ShotCreateRequest.class));
        when(shotRepository.save(any(Shot.class))).thenReturn(testShot);

        // Act
        Shot result = dataService.createShot(validRequest, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testShot, result);
        verify(shotValidator).validate(validRequest);
        verify(shotRepository).save(any(Shot.class));
    }

    @Test
    void testCreateShot_ValidationFails() {
        // Arrange
        doThrow(new RuntimeException("Invalid coordinates"))
                .when(shotValidator).validate(any(ShotCreateRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> dataService.createShot(validRequest, testUser));
        verify(shotValidator).validate(validRequest);
        verify(shotRepository, never()).save(any(Shot.class));
    }
}
