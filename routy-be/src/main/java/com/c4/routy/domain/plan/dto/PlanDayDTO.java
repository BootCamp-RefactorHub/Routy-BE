package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 상세보기에서 DAY 단위 카드 하나의 DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDayDTO {
    private Integer dayId;
    private Integer dayNo;           // 1, 2, 3 ...
    private String date;             // YYYY-MM-DD
    private List<PlanActivityDTO> activities;  // 해당 날짜의 일정들
}
