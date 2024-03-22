package com.twillice.itmoweblab4backspring.controllers;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import com.twillice.itmoweblab4backspring.http.resources.ShotMapper;
import com.twillice.itmoweblab4backspring.http.resources.ShotResource;
import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.repository.ShotRepository;
import com.twillice.itmoweblab4backspring.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my-shots")
@RequiredArgsConstructor
@Tag(name = "User shots")
public class UserShotsController {
    private final ShotMapper shotMapper;
    private final ShotRepository shotRepository;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all user shots")
    public List<ShotResource> retrieveAll() {
        User user = userService.getAuthenticatedUser();
        List<Shot> shots = shotRepository.findByUser(user);
        return shotMapper.toResourceList(shots);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Add a shot in area")
    public ShotResource create(@Valid @RequestBody ShotCreateRequest requestData) {
        User user = userService.getAuthenticatedUser();
        Shot shot = Shot.builder()
                .x(requestData.getX())
                .y(requestData.getY())
                .r(requestData.getR())
                .user(user).build();
        shot = shotRepository.save(shot);
        return shotMapper.toResource(shot);
    }
}
