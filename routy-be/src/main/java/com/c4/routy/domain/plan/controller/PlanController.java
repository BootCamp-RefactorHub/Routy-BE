package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.service.PlanService;
import com.c4.routy.domain.plan.service.PlanServiceImpl;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 일정 삭제
    @PatchMapping("/{planId}/delete")
    public ResponseEntity<Void> softDeletePlan(@PathVariable Integer planId) {
        planService.softDeletePlan(planId);
        return ResponseEntity.ok().build();
    }


    // 일정 공개, 비공개처리
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

    //브라우저 모달 창 좋아요 수 증가 기능
    @PostMapping("/{planId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user) {

        Integer userId = user.getUserNo(); // 로그인된 사용자 번호

        String message = planService.toggleLike(planId, userId);
        int likeCount = planService.getLikeCount(planId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    // 브라우저 지역 드롭 아웃 박스
    @GetMapping("/regions")
    public List<RegionResponseDTO> getAllRegions() {
        return planService.getAllRegions();
    }

    //  조회수 증가 (본인 제외)
    @PostMapping("/{planId}/view")
    public ResponseEntity<Void> increaseViewCount(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer userId = (user != null) ? user.getUserNo() : null;
        planService.increaseViewCount(planId, userId);
        return ResponseEntity.ok().build();
    }

    // 브라우저 북마크 부분
    @PostMapping("/{planId}/bookmark")
    public ResponseEntity<Map<String, Object>> toggleBookmark(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user) {

        Integer userId = user.getUserNo();
        String message = planService.toggleBookmark(planId, userId);
        int bookmarkCount = planService.getBookmarkCount(planId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("bookmarkCount", bookmarkCount);

        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 내가 북마크한 일정 목록
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BrowseResponseDTO>> getUserBookmarks(
            @AuthenticationPrincipal CustomUserDetails user) {
        Integer userId = user.getUserNo();
        List<BrowseResponseDTO> bookmarks = planService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    // 브라우저 있는 일정을 내 일정으로 가져오기 기능(복사 사용)
    @PostMapping("/{planId}/copy")
    public ResponseEntity<Map<String, Object>> copyPlanToMyList(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer userId = user.getUserNo();
        int newPlanId = planService.copyPlanToUser(planId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "일정이 내 일정에 추가되었습니다.");
        response.put("newPlanId", newPlanId);

        return ResponseEntity.ok(response);
    }
}



