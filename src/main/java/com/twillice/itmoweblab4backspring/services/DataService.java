package com.twillice.itmoweblab4backspring.services;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import com.twillice.itmoweblab4backspring.repository.ShotRepository;
import com.twillice.itmoweblab4backspring.validators.ShotValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final ShotRepository shotRepository;
    private final ShotValidator shotValidator;

    public List<Shot> getAllShotsByUser(User user) {
        return shotRepository.findByUser(user);
    }

    public Shot createShot(ShotCreateRequest requestData, User user) {
        shotValidator.validate(requestData);
        Shot shot = Shot.builder()
                .x(requestData.getX())
                .y(requestData.getY())
                .r(requestData.getR())
                .user(user)
                .build();
        return shotRepository.save(shot);
    }
}
