package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


//캘린더(달 단위 일정 목록)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDTO {

    private int year; // 년도
    private int month; // 월
    private List<CalendarPlanDTO> plans; //
}
