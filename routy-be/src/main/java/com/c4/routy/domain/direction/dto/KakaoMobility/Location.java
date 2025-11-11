package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    private double x;       // 경도
    private double y;       // 위도
    private String name;    // 위치 이름
}
