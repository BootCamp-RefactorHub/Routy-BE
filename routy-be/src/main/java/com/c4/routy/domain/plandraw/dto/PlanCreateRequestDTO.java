package com.c4.routy.domain.plandraw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCreateRequestDTO {
    private String planTitle;    // "서울 주말 여행"
    private String startDate;    // LocalDate로 변환 가능한 문자열 (yyyy-MM-dd)
    private String endDate;
    private Integer regionId;    // 선택한 지역
    private Integer userId;      // 로그인 사용자 (개발 중 임시로 1)
}