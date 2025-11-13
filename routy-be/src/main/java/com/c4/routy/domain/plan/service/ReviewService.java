package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.PlanReviewFormDTO;
import com.c4.routy.domain.plan.dto.PlanReviewResponseDTO;
import com.c4.routy.domain.plan.dto.PlanReviewUploadRequestDTO;

import java.util.List;

public interface ReviewService {
    /**
     * 리뷰 작성 모달을 띄울 때, 기존 리뷰 데이터와 파일들을 함께 내려주는 메서드
     * @param planId 일정 식별자
     * @return PlanReviewFormDTO (기존 리뷰 정보 + 이미지들)
     */
    PlanReviewFormDTO getReviewForm(Integer planId);

    /**
     * 리뷰 생성 또는 수정 (파일 업로드 포함)
     * @param dto 요청 데이터 (multipart/form-data)
     * @param loginUserId 로그인 사용자 번호 (JWT에서 꺼내 쓰기)
     * @return 저장된 리뷰 응답 DTO
     */
    PlanReviewResponseDTO createOrUpdateReview(PlanReviewUploadRequestDTO dto, Integer loginUserId);

    PlanReviewResponseDTO getReviewForDisplay(Integer planId);
    List<String> getReviewImageUrls(Integer planId);
}
