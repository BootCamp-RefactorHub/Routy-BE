package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanMapper planMapper;

    /**
     *  상세 조회 (userId + planId 기반)
     * Mapper를 통해 PlanDetailResponseDTO 직접 매핑
     */
    public PlanDetailResponseDTO getPlanDetail(Integer planId) {
        PlanDetailResponseDTO dto = planMapper.selectPlanDetail(planId);

        if (dto == null) {
            throw new IllegalArgumentException("해당 일정의 상세 데이터가 없습니다. (planId=" + planId + ")");
        }

        // 날짜 기반 정보 계산
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
        dto.setDays(days);
        dto.setNights(days - 1);

        // 상태 계산
        LocalDate today = LocalDate.now();
        if (today.isBefore(start)) dto.setStatus("진행예정");
        else if (today.isAfter(end)) dto.setStatus("완료");
        else dto.setStatus("진행중");

        // 기본 설정
        dto.setEditable(true);
        dto.setReviewWritable(true);

        return dto;
    }

    @Transactional
    public void softDeletePlan(Integer planId) {
        PlanDetailResponseDTO dto = planMapper.selectPlanDetail(planId);

        if (dto == null) {
            throw new IllegalArgumentException("존재하지 않는 일정입니다. planId=" + planId);
        }

        if (Boolean.TRUE.equals(dto.getIsDeleted())) {
            throw new IllegalStateException("이미 삭제된 일정입니다. planId=" + planId);
        }

        planMapper.softDeletePlan(planId);
    }

    @Transactional
    public void togglePlanPublic(Integer planId) {
        planMapper.togglePlanPublic(planId);
    }
}