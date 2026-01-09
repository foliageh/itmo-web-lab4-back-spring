package com.twillice.itmoweblab4backspring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.http.resources.TokenResource;
import com.twillice.itmoweblab4backspring.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        UserAuthRequest request = new UserAuthRequest("testuser", "password123");
        TokenResource tokenResource = new TokenResource("test_jwt_token");
        when(authService.register(any(UserAuthRequest.class))).thenReturn(tokenResource);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test_jwt_token"));
    }

    @Test
    void testRegister_InvalidRequest() throws Exception {
        // Arrange
        UserAuthRequest invalidRequest = new UserAuthRequest("ab", "short"); // слишком короткие значения

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_MissingFields() throws Exception {
        // Act & Assert - отправляем пустой JSON
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        UserAuthRequest request = new UserAuthRequest("testuser", "password123");
        TokenResource tokenResource = new TokenResource("test_jwt_token");
        when(authService.login(any(UserAuthRequest.class))).thenReturn(tokenResource);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test_jwt_token"));
    }

    @Test
    void testLogin_InvalidRequest() throws Exception {
        // Arrange
        UserAuthRequest invalidRequest = new UserAuthRequest("", ""); // пустые значения

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
