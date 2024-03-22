package com.twillice.itmoweblab4backspring.http.resources;

import com.twillice.itmoweblab4backspring.model.Shot;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper // (uses = UserMapper.class)
public interface ShotMapper {
    ShotResource toResource(Shot shot);

    List<ShotResource> toResourceList(List<Shot> shots);
}
