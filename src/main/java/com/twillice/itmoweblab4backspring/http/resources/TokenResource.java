package com.twillice.itmoweblab4backspring.http.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Ответ с JWT токеном для аутентификации",
        example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}")
public class TokenResource {
    @Schema(description = "JWT токен для последующих запросов", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    String token;
}
