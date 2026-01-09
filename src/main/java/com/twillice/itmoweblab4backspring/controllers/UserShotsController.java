package com.twillice.itmoweblab4backspring.controllers;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import com.twillice.itmoweblab4backspring.http.resources.ShotMapper;
import com.twillice.itmoweblab4backspring.http.resources.ShotResource;
import com.twillice.itmoweblab4backspring.services.DataService;
import com.twillice.itmoweblab4backspring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-shots")
@RequiredArgsConstructor
@Tag(name = "User Shots", description = "API для управления выстрелами пользователя")
@SecurityRequirement(name = "bearerAuth")
public class UserShotsController {
    private final ShotMapper shotMapper;
    private final DataService dataService;
    private final UserService userService;

    @GetMapping
    @Operation(
        summary = "Получить все выстрелы пользователя",
        description = "Возвращает список всех выстрелов, созданных текущим аутентифицированным пользователем"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка выстрелов",
            content = @Content(schema = @Schema(implementation = ShotResource.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не аутентифицирован (отсутствует или невалидный JWT токен)"
        )
    })
    @ResponseStatus(HttpStatus.OK)
    public List<ShotResource> retrieveAll() {
        var user = userService.getAuthenticatedUser();
        var shots = dataService.getAllShotsByUser(user);
        return shotMapper.toResourceList(shots);
    }

    @PostMapping
    @Operation(
        summary = "Создать новый выстрел",
        description = "Создает новый выстрел в области с указанными координатами (x, y) и радиусом (r). " +
                     "Автоматически вычисляет, попал ли выстрел в область."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Выстрел успешно создан",
            content = @Content(schema = @Schema(implementation = ShotResource.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные запроса (координаты или радиус вне допустимого диапазона)"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Пользователь не аутентифицирован (отсутствует или невалидный JWT токен)"
        )
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ShotResource create(@Valid @RequestBody ShotCreateRequest requestData) {
        var user = userService.getAuthenticatedUser();
        var shot = dataService.createShot(requestData, user);
        return shotMapper.toResource(shot);
    }
}
