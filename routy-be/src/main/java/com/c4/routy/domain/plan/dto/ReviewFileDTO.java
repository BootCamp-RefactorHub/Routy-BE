package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 리뷰에 연결된 파일 정보 DTO
// 조회 응답에 포함
// 리뷰 수정 시에도 기존 파일 리스트로 내려줌
//리뷰에 연결된 파일 정보
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFileDTO {
    private Integer fileId;      // TBL_REVIEWFILES PK
    private String url;          // 보여줄 이미지 경로
    private String originalName; // 원본 파일명 (선택)
}
