package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCopyDTO {
    private Integer planId;       // 새로 생성될 plan_id
    private Integer sourcePlanId; // 복사 원본 plan_id
    private Integer userId;
    private String title;
    private String regionName;
    private String startDate;
    private String endDate;
    private String theme;
}
