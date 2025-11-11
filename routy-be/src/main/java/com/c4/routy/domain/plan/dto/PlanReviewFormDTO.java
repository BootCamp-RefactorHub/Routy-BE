package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 리뷰 작성 모달 처음 띄울 때 내려주는 응답 DTO
// 기존에 리뷰가 있으면 그 내용/별점/파일까지 채워서 보내주고
// 없으면 plan 정보만 채운 상태로 내려보냄
//리뷰 작성 모달 띄울 때 내려주는 응답

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanReviewFormDTO {
    private Integer reviewId;     // 기존 리뷰 있으면 기존리뷰 id값, 새로 쓰는 거면 null값
    private Integer planId;       // 어떤 일정에 대한 리뷰인지
    private String planTitle;     // 모달 상단에 보여줄 제목

    private String content;       // 기존에 쓴 리뷰 내용 (수정 시)
    private Integer rating;       // 1~5

    private List<ReviewFileDTO> files;  // 기존에 업로드됐던 이미지들

}
