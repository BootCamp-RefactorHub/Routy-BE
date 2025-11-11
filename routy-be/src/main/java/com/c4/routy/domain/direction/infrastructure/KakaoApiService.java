package com.c4.routy.domain.direction.infrastructure;

import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteResponse;
import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

public interface KakaoApiService {
    KakaoRouteResponse getDirection(KakaoRouteRequest request);
    int getTimes(Location origin, Location destination);
}
