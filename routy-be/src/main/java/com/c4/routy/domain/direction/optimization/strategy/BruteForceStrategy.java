package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 완전 탐색 전략 (Brute Force)
 * n! 순열 계산으로 최적 경로 탐색
 * n <= 10 정도에만 사용
 */
public class BruteForceStrategy extends RouteStrategy {
    public BruteForceStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder() {
        int n = weights.length;

        // 1. 고정되지 않은 인덱스 수집
        List<Integer> unfixed = new ArrayList<>();
        for(int i = 0;i < n; i++) {
            if(!fixed.contains(i)) unfixed.add(i);
        }

        // 2. 고정되지 않은 부분의 모든 순열 생성
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
                total += weights[order.get(i)][order.get(i + 1)];
            }

            if (total < bestTime) {
                bestTime = total;
                bestOrder = new ArrayList<>(order);
            }
        }

        return bestOrder;
    }

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
