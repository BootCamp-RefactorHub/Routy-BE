package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;

import com.c4.routy.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // 일정 상세 조회
    @GetMapping("/{planId}")
    public PlanDetailResponseDTO getPlanDetail(
            @PathVariable Integer planId) {
        return planService.getPlanDetail(planId);
    }
}