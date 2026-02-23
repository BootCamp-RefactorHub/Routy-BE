package com.c4.routy.domain.direction.dto.KakaoMobility;

import com.c4.routy.domain.direction.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KakaoRouteRequest {

    private Location origin;
    private Location destination;

    @JsonProperty("waypoints")
    private List<Location> wayPoints;

    /* 밑에 두 값을 길 찾기에서 사용되어지는 옵션이다.
     * priority는 경로 선택 기준을 나타내고
     * roadEvent는 도로 이벤트 반영 수준을 나타낸다.
     * 이 두 옵션을 선택 안 할 경우 기본 옵션으로 반영된다.
    * */
    private Priority priority = Priority.RECOMMEND;
    @JsonProperty("roadevent")
    private int roadEvent = 2;
}
