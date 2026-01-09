package com.twillice.itmoweblab4backspring.http.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(description = "Информация о выстреле пользователя",
        example = "{\"id\": 1, \"x\": 1.5, \"y\": 2.0, \"r\": 3.0, \"inArea\": true, \"shotTime\": \"14:30:25\"}")
public class ShotResource {
    @Schema(description = "Уникальный идентификатор выстрела", example = "1", required = true)
    Long id;
    
    @Schema(description = "Координата X точки выстрела", example = "1.5", required = true)
    Double x;
    
    @Schema(description = "Координата Y точки выстрела", example = "2.0", required = true)
    Double y;
    
    @Schema(description = "Радиус области", example = "3.0", required = true)
    Double r;
    
    @Schema(description = "Попадание в область", example = "true", required = true)
    Boolean inArea;
    
    @Schema(description = "Время создания выстрела в формате HH:mm:ss", example = "14:30:25", required = true)
    String shotTime;
}
