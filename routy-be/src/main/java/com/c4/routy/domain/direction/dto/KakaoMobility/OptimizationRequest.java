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
public class OptimizationRequest {
    @JsonProperty("waypoints")
    private List<Location> wayPoints;   // 가고자 하는 모든 지점
    private List<Integer> fixPoints;    // 고정된 지점의 인덱스
    private Priority priority = Priority.RECOMMEND;
}
