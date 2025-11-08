package com.c4.routy.domain.place.service;

import com.c4.routy.domain.place.enums.PlaceCategory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceKakaoFoodService {

    @Value("${kakao.api-key}")
    private String API_KEY;

    @Value("${kakao-url}")
    private String BASE_URL;


    @PostConstruct
    public void init() {
        log.info("Kakao API 설정 확인");
        log.info("API_KEY 존재 여부: {}", API_KEY != null);
    }

    public String findNearbyRestaurants(double latitude, double longitude) {
        try {
            String apiUrl = String.format(
                    "%s?category_group_code=%s&x=%f&y=%f&radius=20000&sort=accuracy",
                    BASE_URL, PlaceCategory.FD6, longitude, latitude
            );

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            String authHeader = "KakaoAK " + API_KEY;
            headers.set("Authorization", authHeader);

            // 상세 디버그 로그
            log.info("URL: {}", apiUrl);
            log.info("Authorization: {}", authHeader);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("응답 성공: {}", response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            log.error("API 호출 실패");
            log.error("에러 타입: {}", e.getClass().getName());
            log.error("에러 메시지: {}", e.getMessage());
            log.error("전체 스택:", e);
            log.error("==================================");
            throw new RuntimeException("카카오 맛집 API 호출 실패: " + e.getMessage(), e);
        }
    }
}