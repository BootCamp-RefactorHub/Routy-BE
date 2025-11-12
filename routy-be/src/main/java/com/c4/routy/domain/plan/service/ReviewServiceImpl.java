package com.c4.routy.domain.plan.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${folder.review}")
    private String folder;

    private final AmazonS3 amazonS3;
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

        // 기존 리뷰 조회 (plan_id로)
        ReviewEntity review;
        if (dto.getReviewId() != null) {
            // reviewId가 있으면 해당 리뷰 수정
            review = reviewRepository.findById(dto.getReviewId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
        } else {
            // reviewId가 없으면 plan으로 기존 리뷰 찾기
            review = reviewRepository.findByPlan(plan).orElse(new ReviewEntity());
        }

        // 필드 세팅
        review.setPlan(plan);
        review.setUser(user);
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        // 리뷰 저장
        reviewRepository.save(review);

        // 여기서 부터 S3
        // 업로드된 파일 URL을 담을 리스트
        List<String> uploadedFileUrls = new ArrayList<>();

        // 업로드할 파일이 비어있지 않으면.
        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            for (MultipartFile file : dto.getFiles()) {

                // S3에 업로드할 파일명 생성
                String fileName = createFileName(file.getOriginalFilename());

                // S3 메타데이터 설정
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try (InputStream inputStream = file.getInputStream()) {
                    // S3에 업로드
                    amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    // S3 URL 가져오기
                    String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

                    // DB에 파일 정보 저장
                    ReviewFileEntity fileEntity = ReviewFileEntity.builder()
                            .review(review)
                            .filePath(fileUrl)  // S3 URL 저장
                            .fileName(file.getOriginalFilename())  // 원본 파일명
                            .fileRename(fileName)  // UUID가 포함된 변환된 파일명 폴더명까지
                            .isDeleted(false)
                            .build();

                    reviewFileRepository.save(fileEntity);

                    // 업로드된 URL을 리스트에 추가
                    uploadedFileUrls.add(fileUrl);

                } catch (IOException e) {
                    log.error("리뷰 파일 업로드 실패: {}", file.getOriginalFilename(), e);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "파일 업로드에 실패했습니다: " + file.getOriginalFilename());
                }

            }
        }

        // 기존 파일 URL도 포함 (삭제되지 않은 파일만)
        if (review.getFiles() != null) {
            List<String> existingFileUrls = review.getFiles().stream()
                    .filter(f -> !f.getIsDeleted())
                    .map(ReviewFileEntity::getFilePath)
                    .toList();

            // 새로 업로드한 파일이 없다면 기존 파일만
            if (uploadedFileUrls.isEmpty()) {
                uploadedFileUrls = existingFileUrls;
            }
        }

        // 응답 조립
        return PlanReviewResponseDTO.builder()
                .reviewId(review.getReviewId())
                .planId(plan.getPlanId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null)
                .files(uploadedFileUrls)  // S3 URL 리스트
                .build();
    }

    // S3 객체 키(사진이름) 만들기 폴더 경로를 추가해야함. s3 버킷에 폴더 나눠놓음.
    private String createFileName(String fileName) {
        return folder + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 확장자 추출
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
