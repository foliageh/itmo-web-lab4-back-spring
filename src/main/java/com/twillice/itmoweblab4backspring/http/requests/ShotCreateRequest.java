package com.twillice.itmoweblab4backspring.http.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Запрос на создание выстрела в области", 
        example = "{\"x\": 1.5, \"y\": 2.0, \"r\": 3.0}")
public class ShotCreateRequest {
    @NotNull(message = "X must not be null")
    @Digits(integer = 1, fraction = 15, message = "X must be decimal with a maximum of 15 fractional digits and 1 integral digit")
    @Schema(description = "Координата X точки выстрела", example = "1.5", required = true,
            type = "number", format = "double", minimum = "-5.0", maximum = "5.0")
    Double x;
    
    @NotNull(message = "Y must not be null")
    @Digits(integer = 1, fraction = 15, message = "Y must be decimal with a maximum of 15 fractional digits and 1 integral digit")
    @Schema(description = "Координата Y точки выстрела", example = "2.0", required = true,
            type = "number", format = "double", minimum = "-5.0", maximum = "5.0")
    Double y;
    
    @NotNull(message = "R must not be null")
    @Digits(integer = 1, fraction = 15, message = "R must be decimal with a maximum of 15 fractional digits and 1 integral digit")
    @Schema(description = "Радиус области", example = "3.0", required = true,
            type = "number", format = "double", minimum = "0.1", maximum = "5.0")
    Double r;
}
