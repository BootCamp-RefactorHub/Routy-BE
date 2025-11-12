package com.c4.routy.domain.plan.mapper;


import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.dto.PlanEditResponseDTO;
import com.c4.routy.domain.plan.dto.PlanReviewFormDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanMapper {

    /**
     * 상세보기
     */
    // 계층형 매핑 결과 직접 DTO로 받음
    PlanDetailResponseDTO selectPlanDetail(@Param("planId") Integer planId);

    void softDeletePlan(@Param("planId") Integer planId);

    void togglePlanPublic(@Param("planId") Integer planId);

    //  공개된 여행 일정 목록 조회 (Browse용)
    List<BrowseResponseDTO> selectPublicPlans(
            @Param("regionId") Integer regionId,
            @Param("days") Integer days,
            @Param("sort") String sort
    );

    // 공개 일정 상세 조회 (모달 전용)
    BrowseDetailResponseDTO selectPublicPlanDetail(@Param("planId") Integer planId);

    // 일정별 Day 조회
    List<PlanDayDTO> selectPlanDays(@Param("planId") Integer planId);

    // Day별 장소 조회
    List<PlanActivityDTO> selectPlanPlaces(@Param("dayId") Integer dayId);

    // 리뷰 조회 (이미지 포함)
    BrowseReviewModalDTO selectPlanReview(@Param("planId") Integer planId);



    /**
     * 수정 페이지 로딩
     */
    PlanEditResponseDTO selectPlanEdit(@Param("planId") Integer planId);

    /**
     * 리뷰 모달
     */
    PlanReviewFormDTO selectReviewForm(@Param("planId") Integer planId);

    // 좋아요 관련 된 매핑
    boolean checkUserLike(@Param("planId") int planId, @Param("userId") int userId);
    void insertLike(@Param("planId") int planId, @Param("userId") int userId);
    void deleteLike(@Param("planId") int planId, @Param("userId") int userId);
    int countLikes(@Param("planId") int planId);

    // ✅ 작성자 ID 조회 (좋아요 방지용)
    Integer selectPlanAuthorId(@Param("planId") Integer planId);

    //  지역 목록 조회 추가
    List<RegionResponseDTO> selectAllRegions();

    // 조회 수 증가
    void incrementViewCount(@Param("planId") Integer planId);

    boolean checkUserBookmark(@Param("planId") int planId, @Param("userId") int userId);
    void insertBookmark(@Param("planId") int planId, @Param("userId") int userId);
    void deleteBookmark(@Param("planId") int planId, @Param("userId") int userId);
    int countBookmarks(@Param("planId") int planId);
    void updateBookmarkCount(@Param("planId") int planId);

    List<BrowseResponseDTO> selectUserBookmarks(@Param("userId") int userId);

    void insertCopiedPlan(PlanCopyDTO dto);

    void copyDurations(@Param("oldPlanId") Integer oldPlanId,
                       @Param("newPlanId") Integer newPlanId);

    void copyTravels(@Param("oldPlanId") Integer oldPlanId,
                     @Param("newPlanId") Integer newPlanId);


}
