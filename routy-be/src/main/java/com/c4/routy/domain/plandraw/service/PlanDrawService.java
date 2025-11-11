package com.c4.routy.domain.plandraw.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plandraw.dto.PlanCreateRequestDTO;
import com.c4.routy.domain.plandraw.dto.PlanResponseDTO;
import com.c4.routy.domain.plandraw.repository.PlanDrawRepository;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service("PlanDrawService")
@RequiredArgsConstructor
public class PlanDrawService {

    private final ModelMapper modelMapper;
    private final PlanDrawRepository planRepository;
    /**
     * 일정 생성 시 Duration(일차) 자동 생성
     */
    @Transactional
    public PlanEntity createPlan(PlanCreateRequestDTO dto) {

        // PlanEntity 생성 및 저장
        PlanEntity plan = new PlanEntity();
        plan.setPlanTitle(dto.getPlanTitle());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setRegionId(dto.getRegionId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Integer userNo = userDetails.getUserNo();

        plan.setUserId(userNo);

        plan.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        PlanEntity savedPlan = planRepository.save(plan);

        return savedPlan;
    }

    // 전체 플랜 조회
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Plan 단건 조회
    public PlanResponseDTO getPlanById(Integer planId) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
        return modelMapper.map(plan, PlanResponseDTO.class);
    }

    // 사용자별 플랜 조회
    public List<PlanResponseDTO> getPlansByUser(Integer userId) {
        return planRepository.findByUserId(userId)
                .stream()
                .map(p -> modelMapper.map(p, PlanResponseDTO.class))
                .collect(Collectors.toList());
    }

}