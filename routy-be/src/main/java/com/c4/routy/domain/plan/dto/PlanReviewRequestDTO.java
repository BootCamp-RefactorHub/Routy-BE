package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//리뷰 등록/수정 요청 DTO (JSON 전용)
// 파일 업로드가 없을 때 사용
// 리뷰 내용/별점만 간단히 보낼 때
//리뷰 등록,수정 요청
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanReviewRequestDTO {
    private Integer reviewId;   // 수정이면 있음, 신규면 null
    private Integer planId; // 어느 플랜에 대한 리뷰인지
    private String content; // 리뷰 본문
    private Integer rating;     // 1~5
}
