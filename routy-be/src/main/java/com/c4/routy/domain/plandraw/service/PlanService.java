package com.c4.routy.domain.plandraw.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.plandraw.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plandraw.dto.PlanResponseDTO;
import com.c4.routy.domain.plandraw.entity.PlanEntity;
import com.c4.routy.domain.plandraw.repository.PlanRepository;
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

    /**
     * 일정 생성 시 Duration(일차)는 자동 생성(duration도메인에서 생성)
     */
    @Transactional
    public PlanEntity createPlan(PlanCreateRequestDTO dto) {

        // PlanEntity 생성 및 저장
        PlanEntity plan = new PlanEntity();
        plan.setPlanTitle(dto.getPlanTitle());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setRegionId(dto.getRegionId());
        plan.setUserId(dto.getUserId() != null ? dto.getUserId() : 1);
        plan.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return  planRepository.save(plan);
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
}
