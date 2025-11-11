package com.c4.routy.domain.direction.enums;

// kakao 길찾기 기준
public enum Priority {
    RECOMMEND,  // 추천 경로
    TIME,       // 최단 시간
    DISTANCE;   // 최단 경로

    public String toString() {
        return name();
    }
}
