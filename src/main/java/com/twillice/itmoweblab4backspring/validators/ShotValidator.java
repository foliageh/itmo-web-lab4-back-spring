package com.twillice.itmoweblab4backspring.validators;

import com.twillice.itmoweblab4backspring.http.requests.ShotCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ShotValidator {
    private static final double MIN_R = 0.1;
    private static final double MAX_R = 5.0;
    private static final double MIN_COORD = -5.0;
    private static final double MAX_COORD = 5.0;

    public void validate(ShotCreateRequest request) {
        validateR(request.getR());
        validateX(request.getX());
        validateY(request.getY());
    }

    private void validateR(Double r) {
        if (r == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "R must not be null");
        }
        if (r < MIN_R || r > MAX_R) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                String.format("R must be between %.1f and %.1f", MIN_R, MAX_R)
            );
        }
    }

    private void validateX(Double x) {
        if (x == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X must not be null");
        }
        if (x < MIN_COORD || x > MAX_COORD) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                String.format("X must be between %.1f and %.1f", MIN_COORD, MAX_COORD)
            );
        }
    }

    private void validateY(Double y) {
        if (y == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Y must not be null");
        }
        if (y < MIN_COORD || y > MAX_COORD) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                String.format("Y must be between %.1f and %.1f", MIN_COORD, MAX_COORD)
            );
        }
    }
}
