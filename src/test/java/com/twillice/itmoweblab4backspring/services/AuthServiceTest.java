package com.twillice.itmoweblab4backspring.services;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.http.resources.TokenResource;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.repository.UserRepository;
import com.twillice.itmoweblab4backspring.security.JWTService;
import com.twillice.itmoweblab4backspring.validators.UserAuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserAuthValidator userAuthValidator;

    @InjectMocks
    private AuthService authService;

    private UserAuthRequest validRequest;
    private User testUser;
    private String encodedPassword;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        validRequest = new UserAuthRequest("testuser", "password123");
        encodedPassword = "encoded_password";
        jwtToken = "test_jwt_token";
        
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password(encodedPassword)
                .roles("USER")
                .build();
    }

    @Test
    void testRegister_Success() {
        // Arrange
        doNothing().when(userAuthValidator).validateRegistration(any(UserAuthRequest.class));
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userService.create(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);

        // Act
        TokenResource result = authService.register(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
        verify(userAuthValidator).validateRegistration(validRequest);
        verify(passwordEncoder).encode("password123");
        verify(userService).create(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void testRegister_ValidationFails() {
        // Arrange
        doThrow(new RuntimeException("User already exists"))
                .when(userAuthValidator).validateRegistration(any(UserAuthRequest.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.register(validRequest));
        verify(userAuthValidator).validateRegistration(validRequest);
        verify(userService, never()).create(any(User.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        doNothing().when(userAuthValidator).validateLogin(any(UserAuthRequest.class));
        doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUser);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

        // Act
        TokenResource result = authService.login(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
        verify(userAuthValidator).validateLogin(validRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtService).generateToken(testUser);
    }

    @Test
    void testLogin_AuthenticationFails() {
        // Arrange
        doNothing().when(userAuthValidator).validateLogin(any(UserAuthRequest.class));
        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(validRequest));
        verify(userAuthValidator).validateLogin(validRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}
