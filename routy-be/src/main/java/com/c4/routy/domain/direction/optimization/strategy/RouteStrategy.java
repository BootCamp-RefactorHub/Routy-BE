package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;
import lombok.Getter;

import java.util.List;

@Getter
public abstract  class RouteStrategy {
    protected List<Location> locations;
    protected List<Integer> fixed;
    protected int[][] weights;

    public RouteStrategy(List<Location> locations, List<Integer> fixed, int[][] weights) {
        this.locations = locations;
        this.fixed = fixed;
        this.weights = weights;
    }

    public List<Location> sort() {
        // 1. 최적 경로 탐색
        List<Integer> bestOrder = findOptimalOrder();

        // 2. 순서에 따라 Location 정렬 후 반환
        return bestOrder.stream()
                .map(this.locations::get)
                .toList();
    }

    // 구현체에서만 오버라이드
    protected abstract List<Integer> findOptimalOrder();
}
