package com.c4.routy.domain.direction.dto.KakaoMobility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Road {
    private String name;
    private int distance;
    private int duration;

    @JsonProperty("traffic_speed")
    private double trafficSpeed;

    @JsonProperty("traffic_state")
    private int trafficState;

    private List<Double> vertexes;
}