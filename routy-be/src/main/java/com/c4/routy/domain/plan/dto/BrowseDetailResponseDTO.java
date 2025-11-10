package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 공개 일정 상세보기용 DTO (모달 상세 화면)
 * PlanDetailResponseDTO 구조 기반
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowseDetailResponseDTO {

    // 기본 정보
    private Integer planId;        // TBL_PLAN.plan_id
    private String title;          // 여행 제목
    private String destination;    // 지역명 (TBL_REGION.region_name)
    private String startDate;      // YYYY-MM-DD
    private String endDate;        // YYYY-MM-DD
    private int days;              // 총 며칠인지 (end-start + 1)
    private String createdAt;
    private Boolean isPublic;      // 공개 여부
    private Boolean isDeleted;     // 삭제 여부

    // 작성자
    private Integer userId;
    private String username;       // 작성자 이름

    // 통계
    private Integer likeCount;     // 좋아요 수
    private Integer bookmarkCount; // 북마크 수
    private Integer viewCount;     // 조회 수


    private BrowseReviewModalDTO review;      // 리뷰 내용

    // 일자별 일정
    private List<PlanDayDTO> dayList; // Day별 장소들
}
