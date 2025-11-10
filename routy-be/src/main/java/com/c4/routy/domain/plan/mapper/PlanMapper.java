package com.c4.routy.domain.plan.mapper;

import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlanMapper {

    // 계층형 매핑 결과 직접 DTO로 받음
    PlanDetailResponseDTO selectPlanDetail(
            @Param("planId") Integer planId);
}
