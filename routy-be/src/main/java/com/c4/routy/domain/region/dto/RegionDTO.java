package com.c4.routy.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionDTO {
    private Long regionId;
    private String regionName;
    private String theme;
    private String regionDesc;
}
