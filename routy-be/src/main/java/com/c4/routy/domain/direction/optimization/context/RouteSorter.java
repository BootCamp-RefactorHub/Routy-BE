package com.c4.routy.domain.direction.optimization.context;

import com.c4.routy.domain.direction.dto.KakaoMobility.Location;
import com.c4.routy.domain.direction.optimization.strategy.RouteStrategy;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class RouteSorter {
    private final RouteStrategy strategy;


    public RouteSorter(RouteStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Location> sort() {

        log.info("========== 정렬 시작 ==========");
        log.info("정렬 전: {}", strategy.getLocations());                // 정렬 전 순서
        
        long start = System.currentTimeMillis();          // 시작 시간 (밀리초)
        List<Location> result = strategy.sort();
        long end = System.currentTimeMillis();            // 끝 시간 (밀리초)
        
        log.info("정렬 후: {}", result);                   // 정렬 후 순서
        log.info("정렬 걸린 시간: {} ms", (end - start));        // 정렬 하는데 걸린 총 시간
        log.info("========== 정렬 종료 ==========");
        
        return result;
    }
}
