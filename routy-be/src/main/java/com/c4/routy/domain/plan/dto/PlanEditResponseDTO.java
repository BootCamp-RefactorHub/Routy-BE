package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// 일정 수정 페이지 첫 로딩 응답

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditResponseDTO {
    private Integer planId;        // 수정할 일정 id
    private String title;          // 여행 일정 제목

    private String destination;    // 여행지 (제주도)

    private int nights;            // 박
    private int days;              // 일

    private String startDate;      // 2024-12-15
    private String endDate;        // 2024-12-18

    // 테마
    private List<String> selectedThemes;     // 사용자가 선택해둔 테마 코드,이름
    private List<ThemeOptionDTO> themeOptions; // 화면에 뿌려줄 전체 테마 목록

    // Day 카드
    private List<PlanEditDayDTO> dayList;
}
