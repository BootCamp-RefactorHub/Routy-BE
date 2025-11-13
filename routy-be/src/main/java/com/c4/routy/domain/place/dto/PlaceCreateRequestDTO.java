package com.c4.routy.domain.place.dto;

import lombok.*;

/**
 * Kakao 장소 선택 시, 일정(planId)에 장소 추가 요청 DTO
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCreateRequestDTO {

    private Integer travelOrder;           // 순서
    private Integer estimatedTravelTime;   // 예상 이동 시간
    private String placeName;              // 장소명
    private Double latitude;               // 위도
    private Double longitude;              // 경도
    private String categoryCode;           // Kakao 카테고리 코드
    private String categoryGroupName;      // Kakao 카테고리명
    private String addressName;            // 주소
    private String placeUrl;               // 지도 URL
    private String description;            // 설명(선택)
    private String imagePath;              // 이미지 경로(선택)
    private String runTime;                // 소요 시간(선택)

    private String startTime;              // 시작 시간
    private String endTime;                // 종료 시간

    private Integer planId;                // 일정 ID (duration_id 조회용)
    private Integer durationId;            // 일차 ID (직접 전달 가능)
    private Integer travelDay;             // 1일차, 2일차
}
