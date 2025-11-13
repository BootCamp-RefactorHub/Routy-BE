package com.c4.routy.domain.place.dto;

import lombok.Data;

@Data
public class KakaoPlaceDocument {
    private String place_name;        // 장소명
    private String category_name;     // 카테고리명
    private String address_name;      // 지번 주소
    private String road_address_name; // 도로명 주소
    private String phone;             // 전화번호
    private String x;                 // 경도 (longitude)
    private String y;                 // 위도 (latitude)
    private String place_url;         // 카카오맵 URL
    private String id;                // 장소 ID
}