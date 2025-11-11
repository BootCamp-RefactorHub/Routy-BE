package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 마이페이지 '내 일정' 요약용 DTO
 * (상세정보 페이지 이동 전, 목록에서 보여줄 데이터)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanSummaryResponseDTO {
    private Integer planId;         // 일정 ID
    private String placeName;           // 여행 제목
    private String regionName;      // 여행 지역명
    private String theme;           // 테마 (힐링, 미식 등)
    private String transportation;  // 교통수단 (버스, 비행기 등)
    private String startDate;       // 시작일
    private String endDate;         // 종료일
    private String status;          // 진행예정 / 진행중 / 완료
}
