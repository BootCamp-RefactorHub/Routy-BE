package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.dto.PlanEditResponseDTO;
import com.c4.routy.domain.plan.dto.PlanEditSaveRequestDTO;
import com.c4.routy.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


// 일정 수정 컨트롤러
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;


    // 상세보기 (일정 상세 조회)
    @GetMapping("/{planId}")
    public PlanDetailResponseDTO getPlanDetail(@PathVariable Integer planId) {
        return planService.getPlanDetail(planId);
    }

    // 수정화면 로딩
    @GetMapping("/{planId}/edit")
    public PlanEditResponseDTO getPlanEdit(@PathVariable Integer planId) {
        return planService.getPlanEdit(planId);
    }

    // 수정 저장
    @PutMapping("/{planId}")
    public void updatePlan(@PathVariable Integer planId,
                           @RequestBody PlanEditSaveRequestDTO dto) {
        dto.setPlanId(planId);
        planService.updatePlan(dto);
    }
}

