package com.twillice.itmoweblab4backspring.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    public static final int TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 1; // 1 day

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }
}
