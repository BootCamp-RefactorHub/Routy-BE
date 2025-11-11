package com.c4.routy.domain.direction.infrastructure;

import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteRequest;
import com.c4.routy.domain.direction.dto.KakaoMobility.KakaoRouteResponse;
import com.c4.routy.domain.direction.dto.KakaoMobility.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class KakaoApiServiceImpl implements KakaoApiService {

    @Value("${maps.kakao.api_key}")
    private String kakaoApiKey;

    @Value("${maps.kakao.directions_uri}")
    private String kakaoDirectionsUri;

    @Value("${maps.kakao.waypoints_uri}")
    private String kakaoWaypointsUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public KakaoRouteResponse getDirection(KakaoRouteRequest request) {

        try{
            String url = kakaoWaypointsUri;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);

            HttpEntity<KakaoRouteRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<KakaoRouteResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    KakaoRouteResponse.class
            );

            return response.getBody();
        } catch (Exception e){
            throw new RuntimeException("Kakao API 호출 중 오류 발생", e);
        }
    }

    @Override
    public int getTimes(Location origin, Location destination) {
        try {
            //  1. GET 요청이므로 쿼리스트링 방식으로 파라미터 설정
            String url = UriComponentsBuilder.fromHttpUrl(kakaoDirectionsUri)
                    .queryParam("origin", origin.getX() + "," + origin.getY())
                    .queryParam("destination", destination.getX() + "," + destination.getY())
                    .queryParam("priority", "RECOMMEND")   // optional
                    .queryParam("car_fuel", "GASOLINE")    // optional
                    .queryParam("summary", true)           // 요약 정보만
                    .build()
                    .toUriString();

            // 2. 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // 3. GET 방식 호출
            ResponseEntity<KakaoRouteResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KakaoRouteResponse.class
            );

            // 4. 응답 파싱
            KakaoRouteResponse body = response.getBody();
            if (body == null || body.getRoutes() == null || body.getRoutes().isEmpty()) {
                throw new RuntimeException("Kakao API 응답이 비어있습니다.");
            }

            int durationSec = body.getRoutes().get(0).getSummary().getDuration();

            log.info("두 지점 간 예상 소요 시간: {}초 ({}분)", durationSec, durationSec / 60);

            return durationSec;

        } catch (Exception e) {
            throw new RuntimeException("Kakao API 호출 중 오류 발생", e);
        }
    }
}
