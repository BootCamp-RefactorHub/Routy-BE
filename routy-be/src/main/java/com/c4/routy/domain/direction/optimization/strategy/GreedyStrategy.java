package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Greedy 전략 (fixed 위치 존중)
 * - fixed 위치 고정
 * - fixed가 있으면 가장 작은 fixed에서 시작, 앞/뒤로 greedy 진행
 */
public class GreedyStrategy extends RouteStrategy {

    public GreedyStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder() {
        int n = weights.length;
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>(Collections.nCopies(n, -1));

        // fixed 위치 고정
        for (int f : fixed) {
            order.set(f, f);
            visited[f] = true;
        }

        if (n == 0) return order;

        // 시작점: 가장 작은 fixed index 또는 0
        int start;
        if (!fixed.isEmpty()) {
            start = Collections.min(fixed);
        } else {
            start = 0;
            order.set(start, start);
            visited[start] = true;
        }

        // greedy 탐색: 앞쪽(-1 방향) 빈 칸 채우기
        int current = start;
        for (int i = start - 1; i >= 0; i--) {
            if (order.get(i) != -1) continue; // fixed 자리면 건너뜀
            int next = findNearest(current, visited, n);
            order.set(i, next);
            visited[next] = true;
            current = next;
        }

        // greedy 탐색: 뒤쪽(+1 방향) 빈 칸 채우기
        current = start;
        for (int i = start + 1; i < n; i++) {
            if (order.get(i) != -1) continue; // fixed 자리면 건너뜀
            int next = findNearest(current, visited, n);
            order.set(i, next);
            visited[next] = true;
            current = next;
        }

        return order;
    }

    // 현재 위치에서 가장 가까운 방문 안 한 노드 찾기
    private int findNearest(int current, boolean[] visited, int n) {
        int minDist = Integer.MAX_VALUE;
        int next = -1;
        for (int i = 0; i < n; i++) {
            if (!visited[i] && weights[current][i] < minDist) {
                minDist = weights[current][i];
                next = i;
            }
        }
        return next;
    }
}
