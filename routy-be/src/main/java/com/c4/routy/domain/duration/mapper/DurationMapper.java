package com.c4.routy.domain.duration.mapper;

import com.c4.routy.domain.duration.entity.DurationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DurationMapper {

    // 특정 planId에 해당하는 duration 목록 조회
    List<DurationEntity> findByPlanId(@Param("planId") Integer planId);

    // planId + day로 duration_id 조회
    Integer findDurationIdByPlanIdAndDay(@Param("planId") Integer planId, @Param("day") Integer day);
}