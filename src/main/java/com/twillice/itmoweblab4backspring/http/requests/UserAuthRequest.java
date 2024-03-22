package com.twillice.itmoweblab4backspring.http.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Authentication / registration request")
public class UserAuthRequest {
    @NotNull(message = "Username must not be null")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Username must contain only letters, numbers and underscores")
    String username;
    @NotNull(message = "Password must not be null")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    String password;
}
