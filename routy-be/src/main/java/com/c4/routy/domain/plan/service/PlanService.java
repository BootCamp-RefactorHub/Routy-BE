package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.duration.service.DurationService;
import com.c4.routy.domain.plan.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.dto.PlanResponseDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.mapper.PlanQueryMapper;
import com.c4.routy.domain.plan.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final ModelMapper modelMapper;
    private final PlanRepository planRepository;
    private final DurationRepository durationRepository;
    private final PlanQueryMapper planQueryMapper;

    /**
     * 일정 생성 시 Duration(일차) 자동 생성
     */
    @Transactional
    public PlanEntity createPlan(PlanCreateRequestDTO dto) {

        // PlanEntity 생성 및 저장
        PlanEntity plan = new PlanEntity();
        plan.setPlanTitle(dto.getPlanTitle());
        plan.setStartTime(dto.getStartDate());
        plan.setEndTime(dto.getEndDate());
        plan.setRegionId(dto.getRegionId());
        plan.setUserId(dto.getUserId() != null ? dto.getUserId() : 1);
        plan.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        PlanEntity savedPlan = planRepository.save(plan);

        // 시작일~종료일 일수 계산
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());
        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;

        // Duration 자동 생성
        for (int i = 1; i <= totalDays; i++) {
            DurationEntity duration = new DurationEntity();
            duration.setDay(i);
            duration.setPlanId(savedPlan.getPlanId());
            durationRepository.save(duration);
        }

        return savedPlan;
    }



    // 전체 플랜 조회
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 사용자별 플랜 조회
    public List<PlanResponseDTO> getPlansByUser(Integer userId) {
        return planRepository.findByUserId(userId)
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 단일 플랜 조회
    public Optional<PlanResponseDTO> getPlanById(Integer planId) {
        return planRepository.findById(planId)
                .map(p -> modelMapper.map(p, PlanResponseDTO.class));
    }

    /**
     *  상세 조회 (userId + planId 기반)
     * Mapper를 통해 PlanDetailResponseDTO 직접 매핑
     */
    public PlanDetailResponseDTO getPlanDetail(Integer planId) {
        PlanDetailResponseDTO dto = planQueryMapper.selectPlanDetail(planId);

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
}