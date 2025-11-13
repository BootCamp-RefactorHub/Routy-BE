package com.c4.routy.domain.plandraw.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponseDTO {
    private Integer planId;
    private String planTitle;
    private String startDate;
    private String endDate;
    private String theme;
    private Integer regionId;
    private Integer userNo;
    private Integer bookmarkCount;
    private Integer viewCount;
    private String createdAt;
}