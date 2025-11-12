package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fixed 위치를 실제로 반영한 안전한 DP 전략
 * - n <= 15 정도에서 효율적
 * - fixed 위치 고정, 전체 최적 경로 계산
 */
public class DPStrategy extends RouteStrategy {

    public DPStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder() {
        int n = weights.length;
        int fullMask = (1 << n) - 1;

        int[][] dp = new int[1 << n][n];
        int[][] parent = new int[1 << n][n];

        // 초기화
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                parent[i][j] = -1;
            }
        }

        // fixed mask
        int fixedMask = 0;
        for (int f : fixed) fixedMask |= (1 << f);

        // 시작점(s) 초기화: fixed와 겹치지 않는 노드
        for (int s = 0; s < n; s++) {
            if (!fixed.contains(s)) {
                dp[(1 << s) | fixedMask][s] = 0;
            }
        }

        // DP 수행
        for (int mask = 0; mask <= fullMask; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) == 0 || dp[mask][u] == Integer.MAX_VALUE) continue;

                for (int v = 0; v < n; v++) {
                    // 이미 방문했거나 fixed 자리 이동 불가
                    if ((mask & (1 << v)) != 0 || fixed.contains(v)) continue;

                    int nextMask = mask | (1 << v);
                    int cost = dp[mask][u] + weights[u][v];

                    if (cost < dp[nextMask][v]) {
                        dp[nextMask][v] = cost;
                        parent[nextMask][v] = u;
                    }
                }
            }
        }

        // 마지막 노드 선택: fixed를 제외한 최종 비용 최소값
        int minCost = Integer.MAX_VALUE;
        int last = -1;
        int targetMask = fullMask;
        for (int i = 0; i < n; i++) {
            if (!fixed.contains(i) && dp[targetMask][i] < minCost) {
                minCost = dp[targetMask][i];
                last = i;
            }
        }

        // backtracking
        List<Integer> order = new ArrayList<>(Collections.nCopies(n, -1));

        // fixed 위치 채우기
        for (int f : fixed) order.set(f, f);

        // DP로 얻은 순서 채우기
        List<Integer> dpOrder = new ArrayList<>();
        int mask = targetMask;
        int cur = last;
        while (cur != -1) {
            if (!fixed.contains(cur)) dpOrder.add(cur);
            int temp = parent[mask][cur];
            mask ^= (1 << cur);
            cur = temp;
        }
        Collections.reverse(dpOrder);

        // order의 -1 자리에 dpOrder 순서대로 채우기
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (order.get(i) == -1) order.set(i, dpOrder.get(idx++));
        }

        return order;
    }
}
