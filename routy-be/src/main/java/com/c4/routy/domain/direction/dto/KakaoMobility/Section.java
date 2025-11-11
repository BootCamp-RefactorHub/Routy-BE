package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Section {
    private int distance;
    private int duration;
    private Bound bound;
    private List<Road> roads;
}
