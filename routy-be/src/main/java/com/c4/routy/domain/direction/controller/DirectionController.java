package com.c4.routy.domain.direction.controller;

import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteResponse;
import com.c4.routy.domain.direction.dto.KakaoMobility.OptimizationRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.OptimizationResponse;
import com.c4.routy.domain.direction.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/direction")
public class DirectionController {

    private final MapService mapService;

    @GetMapping("/health")
    public String health(){
        return "I'm Alive";
    }

    // 현재 순서대로 경로 그리기
    @PostMapping("/points")
    public ResponseEntity<KakaoRouteResponse> getDirection(@RequestBody KakaoRouteRequest request) {
        KakaoRouteResponse result = mapService.getDirection(request);

        return ResponseEntity.ok(result);
    }

    // 주어진 waypoints를 최적화해서 최단거리 만들기
    // 이 때 waypoints중 고정 값이 있을 수 있다. + start와 goal도 waypoints와 같이 최적화 돌려야 함
    @PostMapping("/optimization")
    public ResponseEntity<OptimizationResponse> getOptimization(@RequestBody OptimizationRequest request) {
        OptimizationResponse result = mapService.getOptimization(request);

        return ResponseEntity.ok(result);
    }

}
