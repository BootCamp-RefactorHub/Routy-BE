package com.c4.routy.domain.duration.controller;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.service.DurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plans/{planId}/durations")  // Plan 하위 리소스로 명확하게 표현
@RequiredArgsConstructor
public class DurationController {

    private final DurationService durationService;

    // 특정 일정(planId)의 모든 일차 조회
    @GetMapping
    public ResponseEntity<List<DurationEntity>> getDurationsByPlanId(@PathVariable Integer planId) {
        List<DurationEntity> durations = durationService.findByPlanId(planId);
        return ResponseEntity.ok(durations);
    }

    // 특정 일정(planId)에 일차 생성 (예: 1일차~n일차)
    @PostMapping
    public ResponseEntity<List<DurationEntity>> createDurations(
            @PathVariable Integer planId,
            @RequestBody Map<String, Integer> body) {

        Integer totalDays = body.get("totalDays"); // JSON에서 꺼냄
        List<DurationEntity> created = durationService.createDurations(planId, totalDays);
        return ResponseEntity.ok(created);
    }
}
