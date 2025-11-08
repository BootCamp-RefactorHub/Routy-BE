package com.c4.routy.domain.mypage.dto;

import lombok.*;

//마이페이지 내 일정
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class MyPlanDTO {

    private Integer planId;
    private String title;          // 일정 제목
    private String regionName;     // 지역명 (JOIN REGION)
    private String startDate;      // 시작일
    private String endDate;        // 종료일
    private String durationLabel;  // "ㅇ박 ㅇ일" 같은 일정기간 표시 (duration으로 계산)
    private String status;         // "예정", "진행중", "완료" 화면 상태
}
