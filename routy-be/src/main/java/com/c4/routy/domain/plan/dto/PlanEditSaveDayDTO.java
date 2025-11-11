package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 일정 수정 저장 시 Day 단위로 받는 DTO
// 기존 Day면 dayId가 있고, 새로 추가된 Day면 null
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditSaveDayDTO {
    private Integer dayId;      // 기존 TBL_DURATION PK 있으면 들어옴, 새로 추가된 Day면 null
    private Integer dayNo;      // 1,2,3...
    private String date;        // YYYY-MM-DD 해당 일차 날짜

    // 이 Day 안의 활동들
    private List<PlanEditSaveActivityDTO> activities;
}
