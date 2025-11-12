package com.c4.routy.domain.direction.optimization.factory;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;
import com.c4.routy.domain.direction.optimization.strategy.BitmaskStrategy;
import com.c4.routy.domain.direction.optimization.strategy.BruteForceStrategy;
import com.c4.routy.domain.direction.optimization.strategy.HeuristicStrategy;
import com.c4.routy.domain.direction.optimization.strategy.RouteStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RouteStrategyFactory {


    // 일정 개수 N에 따라서 다른 클래스를 반환해줌
    public static RouteStrategy getRouteStrategy(List<Location> locations,
                                                 List<Integer> fixed,
                                                 int[][] weight,
                                                 int mode) {        // 0 입력시 실사용, 1~3: 테스트
        // 실제 사용
        if(mode == 0) {
            int n = locations.size();

            if (n <= 10) {
                return new BruteForceStrategy(locations, fixed, weight);
            } else if (n <= 15) {
                return new BitmaskStrategy(locations, fixed, weight);
            } else {
                return new HeuristicStrategy(locations, fixed, weight);
            }
        } else {
            switch (mode) {
                case 1 -> {
                    return new BruteForceStrategy(locations, fixed, weight);    // 완점 탐색
                }
                case 2 -> {
                    return new BitmaskStrategy(locations, fixed, weight);       // Bitmask DP
                }
                case 3 -> {
                    return new HeuristicStrategy(locations, fixed, weight);     // Heuristic
                }
                default -> {
                    log.info("모드 설정이 잘못되었습니다.");
                    throw new IllegalArgumentException("모드 설정이 잘못 되었습니다.");
                }
            }
        }
    }
}
