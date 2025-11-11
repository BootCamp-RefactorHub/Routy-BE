package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.PlanReviewFormDTO;
import com.c4.routy.domain.plan.dto.PlanReviewResponseDTO;
import com.c4.routy.domain.plan.dto.PlanReviewUploadRequestDTO;
import com.c4.routy.domain.plan.service.ReviewService;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//리뷰 모달 컨트롤러
@RestController
@RequestMapping("/api/plans/{planId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /** 리뷰 모달 띄울 때 (MyBatis 조회) */
    @GetMapping("/form")
    public PlanReviewFormDTO getReviewForm(@PathVariable Integer planId) {
        return reviewService.getReviewForm(planId);
    }

    /** 리뷰 등록 (파일 포함) */
    @PostMapping
    public PlanReviewResponseDTO uploadReview(
            @PathVariable Integer planId,
            @ModelAttribute PlanReviewUploadRequestDTO dto ,
    @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        // PathVariable로 planId 주입
        dto.setPlanId(planId);

        // 로그인 유저 임시 하드코딩
//        Integer loginUserId = 11;

        Integer loginUserId = userDetails.getUserNo();

        // 서비스에 두 번째 파라미터로 함께 넘기기
        return reviewService.createOrUpdateReview(dto, loginUserId);
    }
}
