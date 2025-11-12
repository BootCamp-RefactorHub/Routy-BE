package com.c4.routy.domain.direction.service;

import com.c4.routy.domain.direction.dto.KakaoMobility.*;
import com.c4.routy.domain.direction.infrastructure.KakaoApiService;
import com.c4.routy.domain.direction.optimization.context.RouteSorter;
import com.c4.routy.domain.direction.optimization.factory.RouteStrategyFactory;
import com.c4.routy.domain.direction.optimization.strategy.RouteStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final KakaoApiService kakaoApiService;


    // TODO: API 호출 횟수를 줄이기 위한 캐싱 로직 추가
    /*
     * Kakao API에서 반환되는 고유 routeId(또는 요청 파라미터 조합)를 키로 사용해,
     * 응답 데이터를 로컬 파일이나 캐시로 저장하는 방식을 고려한다.
     *
     * - 동일한 출발지/도착지 요청이 들어올 경우,
     *   API 재호출 대신 기존 저장 데이터를 반환하도록 처리.
     * - 단, 경로 재탐색이 필요한 상황(예: 위치 변경, 옵션 변경 등)에서는
     *   기존 캐시를 무시하고 새로 호출하도록 한다.
     */
    @Override
    public KakaoRouteResponse getDirection(KakaoRouteRequest request) {
        return kakaoApiService.getDirection(request);
    }

    @Override
    public OptimizationResponse getOptimization(OptimizationRequest request) {
        int n = request.getWayPoints().size();  // 총 지점 개수
        List<Location> points = request.getWayPoints();
        List<Integer> fixed = request.getFixPoints() != null ? request.getFixPoints() : new ArrayList<>();

        // 1. 모든 지점 간 이동 시간 계산
        int[][] times = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    times[i][j] = 0;
                } else {
                    times[i][j] = kakaoApiService.getTimes(points.get(i), points.get(j)); // 카카오 API 호출
                }
            }
        }

        // 2. 팩토리와 전략 패턴을 통해 정렬 로직 선택
        RouteStrategy strategy = RouteStrategyFactory.getRouteStrategy(points, fixed, times, 0);
        RouteSorter sorter = new RouteSorter(strategy);

        // 3. 정렬 진행
        List<Location> ordered = sorter.sort();

        // 4. 최종 경로 요청 (출발지~경유지~도착지)
        KakaoRouteRequest routeReq = new KakaoRouteRequest();
        routeReq.setOrigin(ordered.get(0));
        routeReq.setDestination(ordered.get(ordered.size() - 1));
        if (ordered.size() > 2)
            routeReq.setWayPoints(ordered.subList(1, ordered.size() - 1));

        KakaoRouteResponse routeRes = getDirection(routeReq);

        return new OptimizationResponse(ordered, routeRes);
    }
}