package com.c4.routy.domain.mypage.dto;

import lombok.Builder;
import lombok.Data;


//마이페이지 여행기록
//
@Data
@Builder
public class TravelRecordDTO {

    private Integer planId;        // TBL_PLAN.plan_id
    private String thumbnailUrl;   // TBL_TRAVEL.image_path 중 1개 or null
    private String title;          // TBL_PLAN.plan_title
    private String startTime;      // TBL_PLAN.start_time
    private String endTime;        // TBL_PLAN.end_time
}
