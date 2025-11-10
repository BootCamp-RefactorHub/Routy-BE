package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.BrowseDetailResponseDTO;
import com.c4.routy.domain.plan.dto.BrowseResponseDTO;
import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;

import com.c4.routy.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 일정 삭제
    @PatchMapping("/{planId}/delete")
    public ResponseEntity<Void> softDeletePlan(@PathVariable Integer planId) {
        planService.softDeletePlan(planId);
        return ResponseEntity.ok().build();
    }

    // 일정 공유하기
    @PatchMapping("/{planId}/public")
    public ResponseEntity<Void> togglePlanPublic(@PathVariable Integer planId) {
        planService.togglePlanPublic(planId);
        return ResponseEntity.ok().build();
    }

    // 필터링(최신순, 조회순, 북마크순, 지역, 날짜,) 및 페이징네이션
    @GetMapping("/public")
    public List<BrowseResponseDTO> getPublicPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) Integer days
    ) {
        return planService.getPublicPlans(page, size, sort, regionId, days);
    }

    // 브라우저 상세 모달 보기
    @GetMapping("/public/{planId}")
    public ResponseEntity<BrowseDetailResponseDTO> getPublicPlanDetail(@PathVariable Integer planId) {
        BrowseDetailResponseDTO dto = planService.getPublicPlanDetail(planId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}