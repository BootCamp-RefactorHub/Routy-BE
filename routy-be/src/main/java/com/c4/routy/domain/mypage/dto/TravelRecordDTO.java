package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//마이페이지 여행기록
//일정제목(타이틀), 썸네일 이미지, 일정 시작+종료일
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelRecordDTO {

    private Integer planId;        // TBL_PLAN.plan_id
    private String thumbnailUrl;   // 일정 리뷰 썸네일 TBL_TRAVEL.image_path 첫번째 사진 or [null아님 디폴트값](리뷰없을때)
    private String title;          // 일정 제목 TBL_PLAN.plan_title
    private String startDate;      // 일정 시작일 TBL_PLAN.start_date
    private String endDate;        // 일정 종료일 TBL_PLAN.end_date
}
