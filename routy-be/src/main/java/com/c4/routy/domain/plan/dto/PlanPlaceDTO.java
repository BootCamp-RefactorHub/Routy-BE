package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 하루 일정의 장소 단위 정보
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanPlaceDTO {
    private String name;        // 장소명 (예: 성산일출봉)
    private String address;     // 주소 (예: 제주특별자치도 서귀포시 성산읍)
    private String startTime;   // 시작 시간 (예: 06:00)
    private String endTime;     // 종료 시간 (예: 08:00)
}
