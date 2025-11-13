package com.c4.routy.domain.place.dto;

import lombok.Data;

@Data
public class RestaurantSearchRequest {
    private String query;          // 검색 키워드 (예: "맛집", "이탈리안", "중식당")
    private String category;       // 카테고리 (예: "FD6" 음식점)
    private String x;              // 중심 좌표 X (경도)
    private String y;              // 중심 좌표 Y (위도)
    private Integer radius=5000;        // 반경(m), 0~20000
    private Integer page=1;          // 페이지 번호 (1~45)
    private Integer size=15;          // 한 페이지에 보여질 문서 수 (1~15)
}