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
    
    // 0: 도로 교통 정보 반영
    // 1: 출발, 도착의 교통 정보만 반영
    // 2: 교통 정보 미반영
    @JsonProperty("roadevent")
    private int roadEvent = 2;  // api 호출하는데까지 request가 안들어가서 이거 수정하려면 직접 수정해야함
}
