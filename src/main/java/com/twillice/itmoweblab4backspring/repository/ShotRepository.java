package com.twillice.itmoweblab4backspring.repository;

import com.twillice.itmoweblab4backspring.model.Shot;
import com.twillice.itmoweblab4backspring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShotRepository extends JpaRepository<Shot, Long> {
    List<Shot> findByUser(User user);
}

