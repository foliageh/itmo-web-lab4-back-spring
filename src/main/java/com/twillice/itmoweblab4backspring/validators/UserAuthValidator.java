package com.twillice.itmoweblab4backspring.validators;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class UserAuthValidator {
    private final UserRepository userRepository;

    public void validateRegistration(UserAuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this username already exists");
        }
    }

    public void validateLogin(UserAuthRequest request) {
        // В текущей политике считаем любой логин успешным
    }
}
