package com.c4.routy.domain.place.mapper;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.place.dto.PlaceCreateRequestDTO;
import com.c4.routy.domain.place.entity.PlaceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface PlaceQueryMapper {
    // 특정 planId에 해당하는 duration 목록 조회
    List<DurationEntity> findByPlanId(@Param("planId") Integer planId);

    // planId + day로 duration_id 조회
    Integer findDurationIdByPlanIdAndDay(@Param("planId") Integer planId, @Param("day") Integer day);

    void insertPlacesBatch(@Param("places") List<PlaceCreateRequestDTO> places);
    // 장소 저장
    void insertPlace(PlaceCreateRequestDTO dto);

    // 특정 planId에 속한 장소 목록 조회
    List<PlaceEntity> findPlacesByPlanId(Integer planId);
}