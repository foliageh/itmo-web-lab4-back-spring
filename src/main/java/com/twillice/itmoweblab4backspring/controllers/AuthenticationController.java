package com.twillice.itmoweblab4backspring.controllers;

import com.twillice.itmoweblab4backspring.http.requests.UserAuthRequest;
import com.twillice.itmoweblab4backspring.http.resources.TokenResource;
import com.twillice.itmoweblab4backspring.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создает нового пользователя в системе и возвращает JWT токен для последующей аутентификации"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Успешная регистрация",
            content = @Content(schema = @Schema(implementation = TokenResource.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные запроса (невалидный username или password)"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Пользователь с таким username уже существует"
        )
    })
    @ResponseStatus(HttpStatus.OK)
    public TokenResource register(@Valid @RequestBody UserAuthRequest requestData) {
        return authService.register(requestData);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Аутентификация пользователя",
        description = "Проверяет учетные данные пользователя и возвращает JWT токен для доступа к защищенным ресурсам"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация",
            content = @Content(schema = @Schema(implementation = TokenResource.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные запроса"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Неверные учетные данные (неверный username или password)"
        )
    })
    @ResponseStatus(HttpStatus.OK)
    public TokenResource login(@Valid @RequestBody UserAuthRequest requestData) {
        return authService.login(requestData);
    }
}
