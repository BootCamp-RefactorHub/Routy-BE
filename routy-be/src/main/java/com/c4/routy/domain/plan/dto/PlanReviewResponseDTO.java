package com.c4.routy.domain.plan.dto;

//리뷰 등록 후 다시 돌려줄 응답 DTO

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 리뷰 등록/수정 후 프론트에 다시 돌려줄 응답 DTO
// 방금 저장한 내용을 화면에 바로 반영할 때 사용
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanReviewResponseDTO {
    private Integer reviewId; //  생성/수정된 리뷰 PK
    private Integer planId; // 어떤 플랜의 리뷰인지
    private String content; // 리뷰 내용
    private Integer rating; // 별점
    private String createdAt; // 등록 시각 (포맷은 서비스단에서)

    private List<ReviewFileDTO> files; // 첨부 이미지 목록
}
