package com.c4.routy.domain.direction.optimization.factory;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;
import com.c4.routy.domain.direction.optimization.strategy.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RouteStrategyFactory {

    /**
     * 노드 수와 모드에 따라 적절한 RouteStrategy 반환
     * mode = 0 : 자동 선택
     * mode = 1~5 : 테스트용 강제 선택
     */
    public static RouteStrategy getRouteStrategy(List<Location> locations,
                                                 List<Integer> fixed,
                                                 int[][] weight,
                                                 int mode) {
        int n = locations.size();

        if (mode == 0) { // 자동 선택
            if (n <= 10) {
                log.info("정렬 방법: 완전탐색");
                return new BruteForceStrategy(locations, fixed, weight);
            }
            else if (n <= 15) {
                log.info("정렬 방법: DP탐색");
                return new DPStrategy(locations, fixed, weight);
            }
//            else if (n <= 25) {
            else {
                log.info("정렬 방법: 탐욕적 탐색");
                return new GreedyStrategy(locations, fixed, weight);
            } // Greedy + 2-opt
//            else if (n <= 40) {
//                log.info("정렬 방법: 2-opt");
//                return new TwoOptStrategy(locations, fixed, weight);
//            } // 2-opt Local Search
//            else {
//                log.info("정렬 방법: Genetic");
//                return new GeneticStrategy(locations, fixed, weight);
//            }             // Genetic
        } else { // 테스트 모드
            switch (mode) {
                case 1 -> { return new BruteForceStrategy(locations, fixed, weight); }
                case 2 -> { return new DPStrategy(locations, fixed, weight); }
                case 3 -> { return new GreedyStrategy(locations, fixed, weight); }
//                case 4 -> { return new TwoOptStrategy(locations, fixed, weight); }
//                case 5 -> { return new GeneticStrategy(locations, fixed, weight); }
                default -> {
                    log.info("모드 설정이 잘못되었습니다.");
                    throw new IllegalArgumentException("모드 설정이 잘못 되었습니다.");
                }
            }
        }
    }
}
