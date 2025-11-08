package com.c4.routy.domain.place.service;

import com.c4.routy.domain.duration.mapper.DurationQueryMapper;
import com.c4.routy.domain.place.dto.PlaceCreateRequestDTO;
import com.c4.routy.domain.place.entity.PlaceEntity;
import com.c4.routy.domain.place.mapper.PlaceQueryMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceQueryMapper placeQueryMapper;
    private final DurationQueryMapper durationQueryMapper;

    /** 여러 건 저장 (배치) */
    @Transactional
    public void saveAll(List<PlaceCreateRequestDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) return;

        // 각 장소마다 planId와 travelDay를 기반으로 durationId 자동 세팅
        for (PlaceCreateRequestDTO dto : dtoList) {
            if (dto.getDurationId() == null && dto.getPlanId() != null && dto.getTravelDay() != null) {
                Integer durationId = durationQueryMapper.findDurationIdByPlanIdAndDay(dto.getPlanId(), dto.getTravelDay());
                dto.setDurationId(durationId);
            }
        }

        // 저장
        placeQueryMapper.insertPlacesBatch(dtoList);
    }
    /** 단건 장소 저장 */
    @Transactional
    public void savePlace(PlaceCreateRequestDTO dto) {
        placeQueryMapper.insertPlace(dto);
    }

    /** 일정에 포함된 장소 리스트 조회 */
    public List<PlaceEntity> getPlacesByPlanId(Integer planId) {
        return placeQueryMapper.findPlacesByPlanId(planId);
    }
}
