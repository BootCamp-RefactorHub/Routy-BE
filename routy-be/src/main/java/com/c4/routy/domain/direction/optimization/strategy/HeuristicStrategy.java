package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.List;

// 근사치 구하기 (n이 너무 크면 정확한 해는 구할 수 없고 근사치를 구해줌)
public class HeuristicStrategy extends RouteStrategy {
    public HeuristicStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder(List<Integer> fixed, int[][] weight) {
        return List.of();
    }
}
