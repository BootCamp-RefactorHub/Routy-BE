package com.c4.routy.domain.direction.optimization.strategy;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;

import java.util.List;


public class BitmaskStrategy extends RouteStrategy {

    public BitmaskStrategy(List<Location> locations, List<Integer> fixed, int[][] weight) {
        super(locations, fixed, weight);
    }

    @Override
    protected List<Integer> findOptimalOrder(List<Integer> fixed, int[][] weight) {
        return List.of();
    }
}
