package com.c4.routy.domain.plan.dto;

// 리뷰 등록/수정 - 파일까지 한 번에 받을 때 쓰는 버전
//  POST /api/plans/{planId}/reviews

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 리뷰 등록/수정 - 파일까지 한 번에 받을 때 쓰는 버전
// multipart/form-data 요청을 받을 때 사용
// 모달에서 사진 최대 8장 업로드하는 그 화면과 매칭
// @ModelAttribute로 컨트롤러에서 받는 형태
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanReviewUploadRequestDTO {

    private Integer reviewId;    // 수정이면 값 있고, 신규면 null
    private Integer planId; // 어떤 플랜에 대한 리뷰인지
    private String content; // 리뷰 본문
    private Integer rating;      // 1~5

    // 모달에서 올린 실제 파일들
    private List<MultipartFile> files;
}
