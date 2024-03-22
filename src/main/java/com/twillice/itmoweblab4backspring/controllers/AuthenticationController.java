package com.twillice.itmoweblab4backspring.controllers;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.http.resources.TokenResource;
import com.twillice.itmoweblab4backspring.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "User registration")
    public TokenResource register(@Valid @RequestBody UserAuthRequest requestData) {
        return authenticationService.register(requestData);
    }

    @PostMapping("/login")
    @Operation(summary = "User authentication")
    public TokenResource login(@Valid @RequestBody UserAuthRequest requestData) {
        return authenticationService.login(requestData);
    }
}
