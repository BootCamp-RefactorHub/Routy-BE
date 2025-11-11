package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 일정 수정 화면에서 '저장' 누르면 오는 최상위 요청 DTO
// PUT /api/plans/{planId}
// 프론트에서 보고 있던 화면 구조를 거의 그대로 보낸다 보면 됨
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditSaveRequestDTO {
    private Integer planId;          // 어느 플랜을 저장하는지 (path에도 있지만 body에도 있으면 편함)
    private String title;            // 여행 제목
    private String destination;      // 여행지 이름 (region_name) - 필요 없으면 regionId로 바꿔도 됨
    private String startDate;        // 여행 시작일
    private String endDate;          // 여행 종료일

    private int nights;              // 3박
    private int days;                // 4일

    // 사용자가 선택한 테마 코드들만 보내는 필드
    private List<String> selectedThemes;

    // Day 카드들 (각 Day 안에 활동 포함)
    private List<PlanEditSaveDayDTO> dayList;
}
