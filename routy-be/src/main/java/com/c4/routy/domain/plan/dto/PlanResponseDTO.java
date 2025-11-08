package com.c4.routy.domain.plan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponseDTO {
    private Integer planId;
    private String planTitle;
    private String startTime;
    private String endTime;
    private Integer regionId;
    private Integer userId;
    private Integer bookmarkCount;
    private Integer viewCount;
    private String createdAt;
}
