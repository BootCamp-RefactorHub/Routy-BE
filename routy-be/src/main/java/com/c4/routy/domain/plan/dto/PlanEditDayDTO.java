package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
// "일정 수정" 화면에서 Day 하나를 표현하는 DTO
// 수정 페이지 최초 로딩 응답에서 사용
// 수정 페이지의 Day
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditDayDTO {
    private Integer dayId;      // TBL_DURATION PK (기존 Day면 값 있음)
    private Integer dayNo;     // 1, 2, 3...
    private String date;       // YYYY-MM-DD 해당 일차의 실제 날짜

    private List<PlanEditActivityDTO> activities; // 그 날에 등록된 활동들

}
