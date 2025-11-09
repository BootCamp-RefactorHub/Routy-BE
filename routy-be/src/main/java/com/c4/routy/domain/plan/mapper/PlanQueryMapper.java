package com.c4.routy.domain.plan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlanQueryMapper {

    /* 🔹 일정 상세보기 — planId + userNo */
    List<Map<String, Object>> selectPlanDetailFlat(
            @Param("planId") Integer planId,
            @Param("userNo") Integer userNo
    );

    /* 🔹 마이페이지 – 내 일정 목록 */
    List<Map<String, Object>> selectUserPlans(@Param("userId") Integer userId);
}
