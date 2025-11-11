package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// 일정 상세보기 DTO (전체 응답)
// /api/plans/{planId} 이런 엔드포인트에서 내려줄 용도
// 상단 정보 + Day 리스트를 한 번에 보냄
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailResponseDTO {
    private Integer planId;       // TBL_PLAN.plan_id (일정PK)
    private String title;         // 여행 제목
    private String startDate;     // YYYY-MM-DD
    private String endDate;       // YYYY-MM-DD

    private String status;           // 진행예정 / 진행중 / 완료 (날짜로 계산해서)
    private String destination;      // 여행지 TBL_REGION.region_name
    private String theme;            // 힐링, 가족, 맛집 ...
    private int nights;              // 몇박
    private int days;                // 며칠
    //nights, days는 몇박 며칠인지 표기하는 용도

    private boolean reviewWritable;  // "리뷰 작성하기" 버튼 노출 여부
    private boolean editable;        // 수정/삭제 버튼 노출여부 (작성자일 때 true)

    private List<PlanDayDTO> dayList; // Day 1, Day 2 ...

    private Boolean isDeleted;      // 삭제 여부 판단
    private Boolean isPublic;       // 공유하기 여부 판단
}
