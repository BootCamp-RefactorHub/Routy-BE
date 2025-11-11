package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OptimizationResponse {
    
    private List<Location> locations;   // 순서
    private KakaoRouteResponse route;   // 경로 그리기
}
