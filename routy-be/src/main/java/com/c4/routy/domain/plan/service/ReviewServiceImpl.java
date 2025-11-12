package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.PlanReviewFormDTO;
import com.c4.routy.domain.plan.dto.PlanReviewResponseDTO;
import com.c4.routy.domain.plan.dto.PlanReviewUploadRequestDTO;
import com.c4.routy.domain.plan.dto.ReviewFileDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.entity.ReviewEntity;
import com.c4.routy.domain.plan.entity.ReviewFileEntity;
import com.c4.routy.domain.plan.mapper.PlanMapper;
import com.c4.routy.domain.plan.repository.PlanRepository;
import com.c4.routy.domain.plan.repository.ReviewFileRepository;
import com.c4.routy.domain.plan.repository.ReviewRepository;
import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final PlanMapper planMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;

    // ✅ 새로 추가
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 작성 모달 데이터 조회 (MyBatis)
     */
    @Override
    public PlanReviewFormDTO getReviewForm(Integer planId) {
        return planMapper.selectReviewForm(planId);
    }

    /**
     * 리뷰 생성/수정 (파일 포함)
     */
    @Override
    @Transactional
    public PlanReviewResponseDTO createOrUpdateReview(PlanReviewUploadRequestDTO dto, Integer loginUserId) {

        // Plan / User 조회
        PlanEntity plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("해당 일정(planId=" + dto.getPlanId() + ")이 존재하지 않습니다."));
        UserEntity user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자(userId=" + loginUserId + ")가 존재하지 않습니다."));

        // 신규 or 기존 리뷰 불러오기
        ReviewEntity review = dto.getReviewId() == null
                ? new ReviewEntity()
                : reviewRepository.findById(dto.getReviewId()).orElse(new ReviewEntity());

        // 필드 세팅
        review.setPlan(plan);
        review.setUser(user);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        // 리뷰 저장
        reviewRepository.save(review);

        // 파일 저장
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {
                String filePath = "/uploads/review/" + review.getReviewId() + "/" + file.getOriginalFilename();

                ReviewFileEntity fileEntity = ReviewFileEntity.builder()
                        .review(review)
                        .filePath(filePath)
                        .fileName(file.getOriginalFilename())
                        .fileRename(file.getOriginalFilename())
                        .isDeleted(false)
                        .build();

                reviewFileRepository.save(fileEntity);
            }
        }

        // 응답 조립
        return PlanReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .planId(plan.getPlanId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null)
                .files(review.getFiles() != null
                        ? review.getFiles().stream()
                        .map(f -> ReviewFileDTO.builder()
                                .reviewFileId(f.getReviewfileId())
                                .fileName(f.getFileName())
                                .filePath(f.getFilePath())
                                .fileRename(f.getFileRename())
                                .build())
                        .toList()
                        : List.of()
                )
                .build();
    }
}
