package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.dto.PlanResponseDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.repository.PlanRepository;
import com.c4.routy.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public PlanEntity createPlan(@RequestBody PlanCreateRequestDTO dto) {
        return planService.createPlan(dto);
    }


    // 전체 플랜 조회
    @GetMapping
    public List<PlanResponseDTO> getAllPlans() {
        return planService.getAllPlans();
    }

    // 사용자별 플랜 조회
    @GetMapping("/user/{userId}")
    public List<PlanResponseDTO> getPlansByUser(@PathVariable Integer userId) {
        return planService.getPlansByUser(userId);
    }


    // 일정 상세 조회
    @GetMapping("/{planId}")
    public PlanDetailResponseDTO getPlanDetail(
            @PathVariable Integer planId

    ) {
        return planService.getPlanDetail(planId);
    }
}