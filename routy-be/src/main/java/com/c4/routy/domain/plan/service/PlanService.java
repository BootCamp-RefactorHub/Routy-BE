package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.BrowseDetailResponseDTO;
import com.c4.routy.domain.plan.dto.BrowseResponseDTO;
import com.c4.routy.domain.plan.dto.PlanDayDTO;
import com.c4.routy.domain.plan.dto.PlanDetailResponseDTO;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanMapper planMapper;

    // 상세 조회
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

    // 게시글 소프트 삭제 기능
    public void softDeletePlan(Integer planId) {
        planMapper.softDeletePlan(planId);
    }

    // 공유하기 기능
    public void togglePlanPublic(Integer planId) {
        planMapper.togglePlanPublic(planId);
    }

    // 헤더 부분에 있는 여행 루트 둘러러보기
    public List<BrowseResponseDTO> getPublicPlans(int page, int size, String sort, Integer regionId, Integer days) {
        int offset = page * size;
        return planMapper.selectPublicPlans(offset, size, sort, regionId, days);
    }


    //브라우저 카드 일정 상세 조회 (모달용)
    public BrowseDetailResponseDTO getPublicPlanDetail(Integer planId) {
        //  기본 정보 및 리뷰
        BrowseDetailResponseDTO dto = planMapper.selectPublicPlanDetail(planId);
        if (dto == null) return null;

        //  리뷰 이미지 문자열을 List<String>으로 변환
        if (dto.getReview() != null && dto.getReview().getImages() != null) {
            Object imgField = dto.getReview().getImages();

            if (imgField instanceof String imgStr) {
                dto.getReview().setImages(imgStr);
            }
        }
        // Day 및 장소 목록 구성
        List<PlanDayDTO> dayList = planMapper.selectPlanDays(planId);
        for (PlanDayDTO day : dayList) {
            day.setPlaces(planMapper.selectPlanPlaces(day.getDayId()));
        }
        dto.setDayList(dayList);

        return dto;
    }
}