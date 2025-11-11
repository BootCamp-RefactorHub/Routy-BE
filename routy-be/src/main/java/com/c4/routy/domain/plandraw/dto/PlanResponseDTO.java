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
    private Integer regionId;
    private Integer userId;
    private Integer bookmarkCount;
    private Integer viewCount;
    private String createdAt;
}