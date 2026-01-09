package com.twillice.itmoweblab4backspring.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Getter @Setter(value = AccessLevel.PACKAGE)
@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PACKAGE)
public class User implements UserDetails {
    private Long id;
    @NotNull @Pattern(regexp = "[a-zA-Z0-9_]+")
    private String username;
    @NotNull
    private String password;
    @Builder.Default
    private String roles = "USER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(";")).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
