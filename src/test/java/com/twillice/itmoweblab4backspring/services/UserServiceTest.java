package com.twillice.itmoweblab4backspring.services;

import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .roles("USER")
                .build();
    }

    @Test
    void testSave_NewUser() {
        // Arrange
        User newUser = User.builder()
                .username("newuser")
                .password("password")
                .build();
        when(repository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.save(newUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(repository).save(newUser);
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(repository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.create(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(repository).save(testUser);
    }

    @Test
    void testGetByUsername_Success() {
        // Arrange
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(repository).findByUsername("testuser");
    }

    @Test
    void testGetByUsername_NotFound() {
        // Arrange
        when(repository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> userService.getByUsername("nonexistent"));
        verify(repository).findByUsername("nonexistent");
    }

    @Test
    void testGetAuthenticatedUser_Success() {
        // Arrange
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(repository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getAuthenticatedUser();

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(securityContext).getAuthentication();
        verify(repository).findByUsername("testuser");
    }
}
