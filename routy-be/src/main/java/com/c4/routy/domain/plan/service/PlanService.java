package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.mapper.PlanQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanQueryMapper planQueryMapper;

    public PlanDetailResponseDTO getPlanDetail(Integer planId, Integer userNo) {
        // 1 평면 구조로 가져오기 (MyBatis XML 그대로)
        List<Map<String, Object>> rows = planQueryMapper.selectPlanDetailFlat(planId, userNo);

        if (rows == null || rows.isEmpty()) {
            return null;
        }

        // 2 상위 정보 세팅
        Map<String, Object> first = rows.get(0);

        // 날짜 계산
        String startStr = (String) first.get("start_time");
        String endStr = (String) first.get("end_time");

        // DB 날짜가 "2025-10-10" 형태라면 아래 포맷 그대로 사용 가능
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startStr, formatter);
        LocalDate endDate = LocalDate.parse(endStr, formatter);

        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1; // 시작일 포함
        long nights = Math.max(days - 1, 0);

        PlanDetailResponseDTO detail = PlanDetailResponseDTO.builder()
                .planId((Integer) first.get("plan_id"))
                .title((String) first.get("plan_title"))
                .startDate(startStr)
                .endDate(endStr)
                .destination((String) first.get("region_name"))
                .theme((String) first.get("theme"))
                .days((int) days)
                .nights((int) nights)
                .dayList(new ArrayList<>())
                .build();

        //3. Day 단위 그룹핑
        Map<Integer, PlanDayDTO> dayMap = new LinkedHashMap<>();

        for (Map<String, Object> r : rows) {
            Integer travelDay = (Integer) r.get("travel_day");
            if (travelDay == null) continue;

            // Day 그룹 없으면 새로 추가
            PlanDayDTO dayDTO = dayMap.computeIfAbsent(travelDay, d -> {
                PlanDayDTO dto = new PlanDayDTO();
                dto.setDayId((Integer) r.get("duration_id"));
                dto.setDayNo(travelDay);
                dto.setDate((String) r.get("day_date"));
                dto.setActivities(new ArrayList<>());
                return dto;
            });

            // 각 활동 추가
            PlanActivityDTO activity = PlanActivityDTO.builder()
                    .travelId((Integer) r.get("travel_id"))
                    .title((String) r.get("travel_title"))
                    .place((String) r.get("address_name"))
                    .tag((String) r.get("category_group_name"))
                    .url((String) r.get("place_url"))
                    .build();

            dayDTO.getActivities().add(activity);
        }

        // 최종 DayList 설정
        detail.setDayList(new ArrayList<>(dayMap.values()));

        return detail;
    }

    /*  내 일정 목록 조회 (MyPage용 새 메서드 추가) */
    public List<PlanSummaryResponseDTO> getUserPlans(Integer userId) {
        // MyBatis에서 userId 기반으로 요약 정보 조회
        List<Map<String, Object>> rows = planQueryMapper.selectUserPlans(userId);
        if (rows == null || rows.isEmpty()) return Collections.emptyList();

        // 각 행을 DTO로 변환
        List<PlanSummaryResponseDTO> result = new ArrayList<>();
        for (Map<String, Object> r : rows) {
            result.add(
                    PlanSummaryResponseDTO.builder()
                            .planId((Integer) r.get("plan_id"))
                            .title((String) r.get("plan_title"))
                            .regionName((String) r.get("region_name"))
                            .theme((String) r.get("theme"))
                            .transportation((String) r.get("transportation"))
                            .startDate((String) r.get("start_time"))
                            .endDate((String) r.get("end_time"))
                            .status((String) r.get("status"))
                            .build()
            );
        }

        return result;
    }

}
