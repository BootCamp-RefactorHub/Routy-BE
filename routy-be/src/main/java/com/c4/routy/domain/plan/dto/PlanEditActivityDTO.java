package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//수정 페이지의 활동 한 줄
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEditActivityDTO {
    private Integer travelId;    // TBL_TRAVEL PK (기존 일정이면 값 있고, 새로 추가한 건 null)
    private Integer orderNo;     // 화면에서 위/아래 순서 유지

    private String title;        // 제주 공항 도착
    private String place;        // 제주국제공항
    private String tag;          // 숙소/식당/카페/관광 등

    //일정수정 변경사항
    // address_name(여행장소), category_group_name(카테고리), plcae_name(여행지 제목), plcae_url(여행지 정보url)
    //장소명, 장소 주소, 장소 카테고리, 장소 정보 url 뿌려주기로함

}
