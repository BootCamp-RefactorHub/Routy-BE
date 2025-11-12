package com.c4.routy.domain.place.service;

import com.c4.routy.domain.duration.mapper.DurationMapper;
import com.c4.routy.domain.place.dto.PlaceCreateRequestDTO;
import com.c4.routy.domain.place.dto.PlaceResponseDTO;
import com.c4.routy.domain.place.mapper.PlaceMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceMapper placeMapper;
    private final DurationMapper durationMapper;

    /** 여러 건 저장 (배치) */
    @Transactional
    public void saveAll(List<PlaceCreateRequestDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) return;

        // 각 장소마다 planId와 travelDay를 기반으로 durationId 자동 세팅
        for (PlaceCreateRequestDTO dto : dtoList) {
            if (dto.getDurationId() == null && dto.getPlanId() != null && dto.getTravelDay() != null) {
                Integer durationId = durationMapper.findDurationIdByPlanIdAndDay(dto.getPlanId(), dto.getTravelDay());
                dto.setDurationId(durationId);
            }
        }
        // 저장
        placeMapper.insertPlacesBatch(dtoList);
    }

    /** 일정에 포함된 장소 리스트 조회 */
    public List<PlaceResponseDTO> getPlacesByPlanId(Integer planId) {
        return placeMapper.findPlacesByPlanId(planId);
    }
}
