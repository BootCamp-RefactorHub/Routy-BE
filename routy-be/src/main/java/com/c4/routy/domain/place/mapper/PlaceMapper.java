package com.c4.routy.domain.place.mapper;

import com.c4.routy.domain.place.dto.PlaceCreateRequestDTO;
import com.c4.routy.domain.place.dto.PlaceResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface PlaceMapper {
    void insertPlacesBatch(@Param("places") List<PlaceCreateRequestDTO> places);

    // 특정 planId에 속한 장소 목록 조회
    List<PlaceResponseDTO> findPlacesByPlanId(Integer planId);
}