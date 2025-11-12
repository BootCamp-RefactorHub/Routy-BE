package com.c4.routy.domain.plan.dto;

import lombok.*;


//상세보기에서 일정(활동) 한 줄
//수정 화면에서는 비슷한 구조의 PlanEditActivityDTO 를 따로 씀

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanActivityDTO {
    private Integer travelId;       // TBL_TRAVEL.travel_id (없으면 null)
    private String placeName;       // 장소 이름
    private String addressName;     // 주소
    private String categoryGroup;   // 카테고리 그룹
    private String placeUrl;        // 카카오 상세페이지 URL

    // 추가된 2개의 필드
    private String startTime;       // 장소 시작시간
    private String endTime;         // 장소 종료시간

    private String tag;             // 숙소 / 카페 / 식당 / 관광 / 기타 ... (아이콘 바인딩용)

}
