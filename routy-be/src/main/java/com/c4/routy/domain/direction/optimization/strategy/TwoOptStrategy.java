package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2-opt Local Search
 * 초기 경로 뒤집기를 통해 로컬 최적화
 */
public class TwoOptStrategy extends RouteStrategy {
    public TwoOptStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder() {
        int n = weights.length;
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) order.add(i);

        // fixed 위치는 뒤집기 제외
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                if (fixed.contains(i) || fixed.contains(i + 1)) continue;
                for (int j = i + 2; j < n; j++) {
                    if (fixed.contains(j) || fixed.contains((j+1)%n)) continue;
                    int delta = weights[order.get(i)][order.get(j)] +
                            weights[order.get(i+1)][order.get((j+1)%n)] -
                            weights[order.get(i)][order.get(i+1)] -
                            weights[order.get(j)][order.get((j+1)%n)];
                    if (delta < 0) {
                        Collections.reverse(order.subList(i+1, j+1));
                        improved = true;
                    }
                }
            }
        }
        return order;
    }

}
