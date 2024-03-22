package com.twillice.itmoweblab4backspring.services;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.http.resources.TokenResource;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResource register(UserAuthRequest requestData) {
        var user = User.builder()
                .username(requestData.getUsername())
                .password(passwordEncoder.encode(requestData.getPassword()))
                .build();
        userService.create(user);

        var jwtToken = jwtService.generateToken(user);
        return new TokenResource(jwtToken);
    }

    public TokenResource login(UserAuthRequest requestData) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestData.getUsername(),
                requestData.getPassword()
        ));
        var user = userService.userDetailsService().loadUserByUsername(requestData.getUsername());

        var jwtToken = jwtService.generateToken(user);
        return new TokenResource(jwtToken);
    }
}
