package com.c4.routy.domain.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlaceImageNaverService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.image-url}")
    private String NAVER_IMAGE_API_URL;

    @PostConstruct
    public void init() {
        log.info("네이버 이미지 API 설정 확인");
        log.info("Client ID 존재 여부: {}", clientId != null);
        log.info("Client Secret 존재 여부: {}", clientSecret != null);
        log.info("Image URL: {}", NAVER_IMAGE_API_URL);
    }

    /**
     * 네이버 이미지 검색 API를 호출하여 이미지 URL 반환
     * @param query 검색어 (장소명)
     * @return 이미지 URL (없으면 null)
     */
    public String searchImage(String query) {
        try {
            // URL 생성
            String apiUrl = UriComponentsBuilder
                    .fromHttpUrl(NAVER_IMAGE_API_URL)
                    .queryParam("query", query)
                    .queryParam("display", 1)  // 1개만 가져오기
                    .queryParam("sort", "sim")  // 정확도순
                    .build()
                    .toUriString();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            log.info("네이버 이미지 검색 호출 - 검색어: {}", query);

            // API 호출
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("네이버 이미지 API 응답 성공: {}", response.getStatusCode());

            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode items = root.get("items");

            if (items != null && items.isArray() && items.size() > 0) {
                JsonNode firstItem = items.get(0);
                String imageUrl = firstItem.get("link").asText();
                log.info("이미지 URL 획득 성공: {}", imageUrl);
                return imageUrl;
            }

            log.warn("검색 결과 없음 - 검색어: {}", query);
            return null;

        } catch (Exception e) {
            log.error("네이버 이미지 API 호출 실패 - 검색어: {}", query);
            log.error("에러 타입: {}", e.getClass().getName());
            log.error("에러 메시지: {}", e.getMessage());
            return null;  // 에러 발생 시 null 반환 (기본 이미지 사용)
        }
    }

    /**
     * 여러 장소에 대한 이미지 일괄 검색
     * @param placeNames 장소명 리스트
     * @return 장소명 -> 이미지 URL 맵
     */
    public Map<String, String> searchImagesForPlaces(List<String> placeNames) {
        Map<String, String> imageMap = new HashMap<>();

        for (String placeName : placeNames) {
            String imageUrl = searchImage(placeName);
            if (imageUrl != null) {
                imageMap.put(placeName, imageUrl);
            }
        }

        log.info("총 {}개 장소 중 {}개 이미지 획득", placeNames.size(), imageMap.size());
        return imageMap;
    }
}