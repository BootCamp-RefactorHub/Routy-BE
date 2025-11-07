package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//리뷰 등록,수정 요청
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanReviewRequestDTO {
    private Integer reviewId;   // 수정이면 있음, 신규면 null
    private Integer planId;
    private String content;
    private Integer rating;     // 1~5
}
