package com.c4.routy.domain.direction.dto.KakaoMobility;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KakaoRouteResponse {
    private String transId;
    private List<Routes> routes;
}