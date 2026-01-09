package com.twillice.itmoweblab4backspring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import com.twillice.itmoweblab4backspring.http.resources.ShotMapper;
import com.twillice.itmoweblab4backspring.http.resources.ShotResource;
import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.services.DataService;
import com.twillice.itmoweblab4backspring.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserShotsController.class)
class UserShotsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShotMapper shotMapper;

    @MockBean
    private DataService dataService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Shot testShot;
    private ShotResource shotResource;

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

        shotResource = new ShotResource(1L, 1.5, 2.0, 3.0, true, "12:30:45");
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRetrieveAll_Success() throws Exception {
        // Arrange
        List<Shot> shots = Arrays.asList(testShot);
        List<ShotResource> resources = Arrays.asList(shotResource);
        when(userService.getAuthenticatedUser()).thenReturn(testUser);
        when(dataService.getAllShotsByUser(testUser)).thenReturn(shots);
        when(shotMapper.toResourceList(shots)).thenReturn(resources);

        // Act & Assert
        mockMvc.perform(get("/my-shots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].x").value(1.5))
                .andExpect(jsonPath("$[0].y").value(2.0))
                .andExpect(jsonPath("$[0].r").value(3.0))
                .andExpect(jsonPath("$[0].inArea").value(true));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRetrieveAll_EmptyList() throws Exception {
        // Arrange
        when(userService.getAuthenticatedUser()).thenReturn(testUser);
        when(dataService.getAllShotsByUser(testUser)).thenReturn(Arrays.asList());
        when(shotMapper.toResourceList(any())).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/my-shots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testRetrieveAll_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/my-shots"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreate_Success() throws Exception {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 2.0, 3.0);
        when(userService.getAuthenticatedUser()).thenReturn(testUser);
        when(dataService.createShot(any(ShotCreateRequest.class), any(User.class))).thenReturn(testShot);
        when(shotMapper.toResource(testShot)).thenReturn(shotResource);

        // Act & Assert
        mockMvc.perform(post("/my-shots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.x").value(1.5))
                .andExpect(jsonPath("$.y").value(2.0))
                .andExpect(jsonPath("$.r").value(3.0))
                .andExpect(jsonPath("$.inArea").value(true));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testCreate_InvalidRequest() throws Exception {
        // Arrange
        ShotCreateRequest invalidRequest = new ShotCreateRequest(null, 2.0, 3.0); // null x

        // Act & Assert
        mockMvc.perform(post("/my-shots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreate_Unauthorized() throws Exception {
        // Arrange
        ShotCreateRequest request = new ShotCreateRequest(1.5, 2.0, 3.0);

        // Act & Assert
        mockMvc.perform(post("/my-shots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
