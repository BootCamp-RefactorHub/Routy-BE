package com.c4.routy.domain.direction.dto.KakaoMobility;

import com.c4.routy.domain.direction.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoutesSummary {
    private Location origin;
    private Location destination;
    @JsonProperty("waypoints")
    private List<Location> wayPoints;
    private Priority priority;
    private Bound bound;
    private int distance;
    private int duration;
}