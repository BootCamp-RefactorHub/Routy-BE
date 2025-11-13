package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// "일정 수정" 페이지를 처음 열 때 내려주는 전체 응답
// 상단의 여행정보 카드 + Day 카드들 + 테마 옵션
// 프론트에서 이거 한 번 받으면 화면 전체를 그릴 수 있음
// 일정 수정 페이지 첫 로딩 응답

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditResponseDTO {

    private Integer planId;        // 수정 대상이 되는 플랜 ID
    private String title;          // 여행 일정 제목

    private String destination;    // 여행지이름

    private int nights;            // 박
    private int days;              // 일

    private String startDate;      // 여행 시작일
    private String endDate;        // 여행 종료일
    private String theme;
    // 테마
    private List<String> selectedThemes;     // 사용자가 선택해둔 테마 코드,이름 (화면에서 체크 상태로 표시)
    private List<ThemeOptionDTO> themeOptions; // 화면에 뿌려줄 전체 테마 목록 (멀티 선택 가능)

    // Day 카드
    private List<PlanEditDayDTO> dayList;
}
