package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// "일정 수정" 화면에서 한 줄(카드 안의 활동)을 표현하는 DTO
//  수정 페이지 최초 로딩 시 내려주는 응답용
//수정 페이지의 활동 한 줄

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditActivityDTO {
    private Integer travelId;    // TBL_TRAVEL PK (기존 일정이면 값 있고, 새로 추가한 건 null)
    private Integer orderNo;     // 화면에서 위/아래 순서 (드래그&드롭 시 정렬용)



    private String addressName;        // address_name   카카오 장소검색: 전체 주소
    private String categoryGroupName;  // category_group_name  카카오 장소검색: 대분류 카테고리명
    private String placeName;          // place_name  카카오 장소검색: 장소 이름
    private String placeUrl;           // place_url 카카오 장소검색: 상세보기 URL

    private String tag;          // 숙소/식당/카페/관광 등 아이콘 바인딩용

}
