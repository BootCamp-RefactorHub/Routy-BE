package com.c4.routy.domain.duration.service;

import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.mapper.DurationMapper;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DurationService {

    private final DurationRepository durationRepository;
    private final DurationMapper durationMapper;
    private final PlanRepository planRepository;

    public List<DurationEntity> findByPlanId(Integer planId) {
        return durationMapper.findByPlanId(planId);
    }

    // 일정 생성 시 총 일수(dayCount)에 맞게 Duration 자동 생성
    public List<DurationEntity> createDurations(Integer planId, int dayCount) {
        List<DurationEntity> durations = new ArrayList<>();

        // ✅ planId를 통해 PlanEntity 조회
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 planId가 존재하지 않습니다: " + planId));

        LocalDate startDate = LocalDate.parse(plan.getStartDate());

        for (int i = 1; i <= dayCount; i++) {
            LocalDate currentDate = startDate.plusDays(i - 1);

            // DateTimeUtil이 LocalDateTime만 받으면 이 한 줄 추가
            String formattedDate = DateTimeUtil.format(currentDate.atStartOfDay()); // "yyyy-MM-dd"

            DurationEntity duration = DurationEntity.builder()
                    .day(i)                  // day_no 같은 필드
//                    .date(formattedDate)     // TBL_DURATION.date NOT NULL이면 꼭 넣기
                    .plan(plan)              //
                    .build();

            durations.add(durationRepository.save(duration));
        }

        return durations;
    }
}
