package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowseResponseDTO {
    private Integer planId;          // 일정 ID
    private String title;            // 제목
    private String destination;      // 지역명
    private String startDate;        // 시작일
    private String endDate;          // 종료일
    private Integer days;            // 며칠
    private String userNickname;     // 작성자
    private Integer likeCount;       // 좋아요 수 (tbl_like)
    private Integer bookmarkCount;   // 북마크 수 (tbl_plan.bookmark_count)
    private Integer viewCount;       // 조회 수 (tbl_plan.view_count)
    private Boolean isPublic;        // 공개 여부
    private Integer placeCount;      // 총 day 별 총 일정 수
    private Boolean isBookmarked;    // 북마크 확인 여부
    private List<String> reviewImages;  // 리뷰에 저장되는 많은 사진
    private String reviewImagesRaw;
    private String defaultPlanImage;
}

