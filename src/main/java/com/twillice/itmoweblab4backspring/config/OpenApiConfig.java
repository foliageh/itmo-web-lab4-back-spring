package com.twillice.itmoweblab4backspring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "ITMO Web Lab 4 Backend API",
        version = "1.0.0",
        description = "REST API для управления выстрелами пользователей в области. " +
                      "Поддерживает регистрацию, аутентификацию через JWT и управление выстрелами.",
        contact = @Contact(
            name = "ITMO Web Lab 4",
            url = "https://github.com/foliageh/itmo-web-lab4"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080/api",
            description = "Локальный сервер разработки"
        )
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT токен для аутентификации. Получите токен через /auth/register или /auth/login"
)
public class OpenApiConfig {
}
