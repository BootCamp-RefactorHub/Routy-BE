package com.c4.routy.domain.place.service;

import com.c4.routy.domain.place.enums.PlaceCategory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoHotelService {

    @Value("${kakao.api-key}")
    private String API_KEY;

    @Value("${kakao-url}")
    private String BASE_URL;


    @PostConstruct
    public void init() {
        log.info("Kakao API 설정 확인");
        log.info("API_KEY 존재 여부: {}", API_KEY != null);
    }

    public String findNearbyHotels(double latitude, double longitude) {
        try {
            List<Object> allDocuments = new ArrayList<>();  // 전체 결과 저장

            for (int page = 1; page <= 3; page++) {
                String apiUrl = String.format(
                        "%s?category_group_code=%s&x=%f&y=%f&radius=10000&size=15&page=%d&sort=accuracy",
                        BASE_URL, PlaceCategory.AD5, longitude, latitude, page  // page 변수 사용
                );

                // 헤더 설정
                HttpHeaders headers = new HttpHeaders();
                String authHeader = "KakaoAK " + API_KEY;
                headers.set("Authorization", authHeader);

                // 상세 디버그 로그
                log.info("페이지 {} 호출 - URL: {}", page, apiUrl);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<String> response = restTemplate.exchange(
                        apiUrl,
                        HttpMethod.GET,
                        entity,
                        String.class
                );

                log.info("페이지 {} 응답 성공: {}", page, response.getStatusCode());

                // JSON 파싱해서 documents만 추출
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode documents = root.get("documents");

                if (documents != null && documents.isArray()) {
                    for (JsonNode doc : documents) {
                        allDocuments.add(mapper.treeToValue(doc, Object.class));
                    }
                    log.info("페이지 {} - {}개 결과 추가됨", page, documents.size());
                }

                // 마지막 페이지 체크
                JsonNode meta = root.get("meta");
                if (meta != null && meta.get("is_end").asBoolean()) {
                    log.info("마지막 페이지 도달, 총 {}개 결과", allDocuments.size());
                    break;
                }
            }

            // 전체 결과 반환 (JSON 형태로)
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = new HashMap<>();
            result.put("documents", allDocuments);
            result.put("meta", Map.of("total_count", allDocuments.size()));

            return mapper.writeValueAsString(result);

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