package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.entity.TravelEntity;
import com.c4.routy.domain.plan.mapper.PlanMapper;
import com.c4.routy.domain.plan.repository.PlanRepository;
import com.c4.routy.domain.plan.repository.TravelRepository;
import com.c4.routy.domain.region.entity.RegionEntity;
import com.c4.routy.domain.region.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final DurationRepository DurationRepository;
    private final TravelRepository travelRepository;
    private final RegionRepository RegionRepository;

    /**
     * 상세 조회
     * - mapper에서 기본 정보 가져오고
     * - 다른 브랜치에서 하던 일수/박수/상태 계산까지 여기서 처리
     */
    @Override
    public PlanDetailResponseDTO getPlanDetail(Integer planId) {
        PlanDetailResponseDTO dto = planMapper.selectPlanDetail(planId);

        if (dto == null) {
            throw new IllegalArgumentException("해당 일정의 상세 데이터가 없습니다. (planId=" + planId + ")");
        }

        // startDate / endDate 는 mapper에서 문자열로 온다고 가정 (yyyy-MM-dd)
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());

        // 몇일짜리 여행인지
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
        dto.setDays(days);
        dto.setNights(days - 1);

        // 상태값 계산
        LocalDate today = LocalDate.now();
        if (today.isBefore(start)) {
            dto.setStatus("진행예정");
        } else if (today.isAfter(end)) {
            dto.setStatus("완료");
        } else {
            dto.setStatus("진행중");
        }

        // 프론트에서 쓸 플래그 기본값
        dto.setEditable(true);
        dto.setReviewWritable(true);

        return dto;
    }

    @Override
    public PlanEditResponseDTO getPlanEdit(Integer planId) {
        return planMapper.selectPlanEdit(planId);
    }

    /**
     * 일정 수정 저장 로직
     * - 기존 Day/Travel 싹 지우고 새로 INSERT 하는 방식 (가장 단순, 프론트 구조랑 1:1 매칭)
     */
    @Override
    @Transactional
    public void updatePlan(PlanEditSaveRequestDTO dto) {

        PlanEntity plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("플랜을 찾을 수 없습니다."));

        // 제목/기간 변경
        plan.changeTitle(dto.getTitle());
        plan.changePeriod(dto.getStartDate(), dto.getEndDate());

        // 지역 변경
        if (dto.getDestination() != null) {
            RegionEntity region = RegionRepository.findByRegionName(dto.getDestination());
            plan.setRegion(region);
        }

        // 기존 Day/Travel 다 삭제
        List<DurationEntity> oldDays = DurationRepository.findByPlan(plan);
        for (DurationEntity oldDay : oldDays) {
            travelRepository.deleteByDuration(oldDay);
        }
        DurationRepository.deleteByPlan(plan);

        // 새로 insert
        for (PlanEditSaveDayDTO dayDTO : dto.getDayList()) {
            DurationEntity dayEntity = DurationEntity.builder()
                    .plan(plan)
                    .day(dayDTO.getDayNo())
                    .build();
            DurationRepository.save(dayEntity);

            for (PlanEditSaveActivityDTO actDTO : dayDTO.getActivities()) {
                TravelEntity travel = TravelEntity.builder()
                        .duration(dayEntity)
                        .placeName(actDTO.getPlaceName())
                        .addressName(actDTO.getAddressName())
                        .categoryGroupName(actDTO.getCategoryGroupName())
                        .placeUrl(actDTO.getPlaceUrl())
                        .tag(actDTO.getTag())
                        .travelOrder(actDTO.getTravelOrder())  // 엔티티에 맞게 수정
                        .build();
                travelRepository.save(travel);
            }
        }

        planRepository.save(plan);
    }
}
