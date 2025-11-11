package com.c4.routy.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionALLDTO {
    private Integer regionId;
    private String regionName;
    private String adminCode;
    private String theme;
    private String regionDesc;
    private Double startLat;
    private Double startLng;
}
