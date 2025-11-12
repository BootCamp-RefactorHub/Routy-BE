package com.c4.routy.domain.plan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 일정 수정 화면에서 "저장" 눌렀을 때 한 줄(활동) 단위로 서버에 넘기는 DTO
// PlanEditActivityDTO와 비슷하지만 "요청" 전용
// 서비스단에서는 travelId 존재 여부로 INSERT/UPDATE 구분
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditSaveActivityDTO {
    private Integer travelId;         // 기존 일정이면 있음, 새로 추가면 null
    private Integer travelOrder;          // 위/아래 정렬용 (같은 Day 안에서의 순서)

    private String tag;               // 숙소/식당/카페/관광 등

    private String startTime;
    private String endTime;

    // ===== 카카오 장소 정보 =====
    private String placeName;         // place_name
    private String addressName;       // address_name
    private String categoryGroupName; // category_group_name
    private String placeUrl;          // place_url

}
