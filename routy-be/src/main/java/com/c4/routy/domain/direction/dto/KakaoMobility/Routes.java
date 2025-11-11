package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Routes {
    private int resultCode;
    private String resultMsg;
    private RoutesSummary summary;
    private List<Section> sections;
}
