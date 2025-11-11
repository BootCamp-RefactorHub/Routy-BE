package com.c4.routy.domain.direction.service;

import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteResponse;
import com.c4.routy.domain.direction.dto.KakaoMobility.OptimizationRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.OptimizationResponse;

public interface MapService {
    KakaoRouteResponse getDirection(KakaoRouteRequest request);

    OptimizationResponse getOptimization(OptimizationRequest request);
}