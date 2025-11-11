package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Bound {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
}
