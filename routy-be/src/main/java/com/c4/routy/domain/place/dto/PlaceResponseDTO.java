package com.c4.routy.domain.place.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PlaceResponseDTO {

    private Integer travelId;
    private Integer travelOrder;
    private Integer estimatedTravelTime;
    private String placeName;
    private Double latitude;
    private Double longitude;
    private String categoryCode;
    private String categoryGroupName;
    private String addressName;
    private String placeUrl;
    private String description;
    private String imagePath;
    private String runTime;
    private Integer durationId;
    private String startTime;
    private String endTime;
}