package com.c4.routy.domain.duration.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.mapper.DurationMapper;
import com.c4.routy.domain.duration.repository.DurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DurationService {

    private final DurationRepository durationRepository;
    private final DurationMapper durationMapper;

    public List<DurationEntity> findByPlanId(Integer planId) {
        return durationMapper.findByPlanId(planId);
    }

    // 일정 생성 시 총 일수(dayCount)에 맞게 Duration 자동 생성
    public List<DurationEntity> createDurations(Integer planId, int dayCount) {
        List<DurationEntity> durations = new ArrayList<>();

        for (int i = 1; i <= dayCount; i++) {
            DurationEntity d = new DurationEntity();
            d.setDay(i);
            d.setPlanId(planId);
            durations.add(durationRepository.save(d));
        }

        return durations;
    }
}
