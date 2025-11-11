package com.c4.routy.domain.direction.service;

import com.c4.routy.domain.direction.dto.KakaoMobility.*;
import com.c4.routy.domain.direction.infrastructure.KakaoApiService;
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

        // 2. 최적 경로 탐색 (고정된 지점 유지)
        List<Integer> bestOrder = findOptimalOrder(times, fixed);

        // 3. 순서에 따라 정렬된 지점 리스트 구성
        List<Location> ordered = bestOrder.stream()
                .map(points::get)
                .toList();

        // 4. 최종 경로 요청 (출발지~경유지~도착지)
        KakaoRouteRequest routeReq = new KakaoRouteRequest();
        routeReq.setOrigin(ordered.get(0));
        routeReq.setDestination(ordered.get(ordered.size() - 1));
        if (ordered.size() > 2)
            routeReq.setWayPoints(ordered.subList(1, ordered.size() - 1));

        KakaoRouteResponse routeRes = getDirection(routeReq);

        return new OptimizationResponse(ordered, routeRes);
    }

    /**
     * 최적 순서 탐색 (고정 인덱스 유지)
     * fixPoints는 "원래 위치를 그대로 유지할 인덱스"로 처리한다.
     */
    private List<Integer> findOptimalOrder(int[][] times, List<Integer> fixed) {
        int n = times.length;

        // 고정되지 않은 인덱스 수집
        List<Integer> unfixed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!fixed.contains(i)) unfixed.add(i);
        }

        // 고정되지 않은 부분의 모든 순열 생성
        List<List<Integer>> permutations = new ArrayList<>();
        permute(unfixed, 0, permutations);

        int bestTime = Integer.MAX_VALUE;
        List<Integer> bestOrder = null;

        // 각 순열에 대해 총 이동 시간 계산
        for (List<Integer> perm : permutations) {
            List<Integer> order = new ArrayList<>(Collections.nCopies(n, -1));
            int permIdx = 0;

            // 1. 고정 지점은 그대로 유지
            for (int fixedIdx : fixed) {
                order.set(fixedIdx, fixedIdx);
            }

            // 2. 나머지 자리에 순열 값 채우기
            for (int i = 0; i < n; i++) {
                if (!fixed.contains(i)) {
                    order.set(i, perm.get(permIdx++));
                }
            }

            // 3. 총 이동 시간 계산
            int total = 0;
            for (int i = 0; i < n - 1; i++) {
                total += times[order.get(i)][order.get(i + 1)];
            }

            if (total < bestTime) {
                bestTime = total;
                bestOrder = new ArrayList<>(order);
            }
        }

        return bestOrder;
    }

    /**
     * 순열 생성 (DFS)
     */
    private void permute(List<Integer> arr, int k, List<List<Integer>> result) {
        if (k == arr.size()) {
            result.add(new ArrayList<>(arr));
        } else {
            for (int i = k; i < arr.size(); i++) {
                Collections.swap(arr, i, k);
                permute(arr, k + 1, result);
                Collections.swap(arr, i, k);
            }
        }
    }
}