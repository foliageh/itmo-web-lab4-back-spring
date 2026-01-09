package com.twillice.itmoweblab4backspring.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter(value = AccessLevel.PACKAGE)
@Builder(toBuilder = true) @NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Shot {
    private Long id;
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private Double r;
    private Boolean inArea;
    private String shotTime;
    private User user;
}
