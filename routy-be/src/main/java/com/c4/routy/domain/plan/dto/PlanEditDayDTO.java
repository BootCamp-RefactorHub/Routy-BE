package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
// 수정 페이지의 Day
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditDayDTO {
    private Integer dayId;
    private Integer dayNo;     // 1, 2, 3...
    private String date;       // YYYY-MM-DD

    private List<PlanEditActivityDTO> activities;
}
