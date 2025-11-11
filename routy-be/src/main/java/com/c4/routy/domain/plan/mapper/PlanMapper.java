package com.c4.routy.domain.plan.mapper;

import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.dto.PlanEditResponseDTO;
import com.c4.routy.domain.plan.dto.PlanReviewFormDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanMapper {

    /** 상세보기 */
    // 계층형 매핑 결과 직접 DTO로 받음
    PlanDetailResponseDTO selectPlanDetail(@Param("planId") Integer planId);

    /** 수정 페이지 로딩 */
    PlanEditResponseDTO selectPlanEdit(@Param("planId") Integer planId);

    /** 리뷰 모달 */
    PlanReviewFormDTO selectReviewForm(@Param("planId") Integer planId);
}
