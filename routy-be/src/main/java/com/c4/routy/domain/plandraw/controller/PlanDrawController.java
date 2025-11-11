package com.c4.routy.domain.plandraw.controller;

import com.c4.routy.domain.plandraw.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plandraw.dto.PlanResponseDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plandraw.service.PlanDrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("PlanDrawController")
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanDrawController {

    private final PlanDrawService planDrawService;

    // 플랜 생성
    @PostMapping
    public PlanEntity createPlan(@RequestBody PlanCreateRequestDTO dto) {
        return planDrawService.createPlan(dto);
    }

    // 전체 플랜 조회
    @GetMapping
    public List<PlanResponseDTO> getAllPlans() {
        return planDrawService.getAllPlans();
    }

    // Plan 단건 조회 (regionId 포함)
    @GetMapping("/select/{planId}")
    public ResponseEntity<PlanResponseDTO> getPlanById(@PathVariable Integer planId) {
        PlanResponseDTO plan = planDrawService.getPlanById(planId);
        return ResponseEntity.ok(plan);
    }
    // 사용자별 플랜 조회
    @GetMapping("/user/{userId}")
    public List<PlanResponseDTO> getPlansByUser(@PathVariable Integer userId) {
        return planDrawService.getPlansByUser(userId);
    }

}
