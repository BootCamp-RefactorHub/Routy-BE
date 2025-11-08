package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//여행 달력
//CalendarDTO 내부 list
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarPlanDTO {

    private Integer id;   // TBL_PLAN.plan_id
    private String title; //일정 제목
    private String startDate;   // TBL_PLAN.start_time
    private String endDate;     // TBL_PLAN.end_time

}
