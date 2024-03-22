package com.twillice.itmoweblab4backspring.http.resources;

import lombok.Value;

@Value
public class ShotResource {
    Long id;
    Double x;
    Double y;
    Double r;
    Boolean inArea;
    String shotTime;
}
