package com.c4.routy.domain.place.service;

import com.c4.routy.domain.place.dto.KakaoPlaceResponse;
import com.c4.routy.domain.place.dto.KakaoSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class KakaoSearchService {

    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    private static final String KAKAO_LOCAL_SEARCH_API = "https://dapi.kakao.com/v2/local/search/keyword.json";

    private final RestTemplate restTemplate;

    public KakaoSearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 카카오 API로 맛집 검색
     */
    public KakaoPlaceResponse searchRestaurants(KakaoSearchRequest request) {
        try {
            // URL 빌드
            String url = buildSearchUrl(request);

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            log.info("카카오 API 호출: {}", url);
            KakaoPlaceResponse response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    KakaoPlaceResponse.class
            ).getBody();

            log.info("검색 결과: {} 건", response != null ? response.getDocuments().size() : 0);
            return response;

        } catch (Exception e) {
            log.error("카카오 API 호출 실패", e);
            throw new RuntimeException("맛집 검색 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 카테고리로 맛집 검색 (음식점 카테고리만)
     */
    public KakaoPlaceResponse searchRestaurantsByCategory(KakaoSearchRequest request) {
        request.setCategory("FD6"); // 음식점 카테고리 코드
        return searchRestaurants(request);
    }

    /**
     * 위치 기반 주변 맛집 검색
     */
    public KakaoPlaceResponse searchNearbyRestaurants(String x, String y, Integer radius, Integer page, Integer size) {
        KakaoSearchRequest request = new KakaoSearchRequest();
        request.setQuery("맛집");
        request.setCategory("FD6");
        request.setX(x);
        request.setY(y);
        request.setRadius(radius != null ? radius : 5000);
        request.setPage(page != null ? page : 1);
        request.setSize(size != null ? size : 15);

        return searchRestaurants(request);
    }

    /**
     * URL 빌드
     */
    private String buildSearchUrl(KakaoSearchRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_API)
                .queryParam("query", request.getQuery())
                .queryParam("page", request.getPage())
                .queryParam("size", request.getSize());

        if (request.getCategory() != null) {
            builder.queryParam("category_group_code", request.getCategory());
        }

        if (request.getX() != null && request.getY() != null) {
            builder.queryParam("x", request.getX())
                    .queryParam("y", request.getY());

            if (request.getRadius() != null) {
                builder.queryParam("radius", request.getRadius());
            }
        }

        return builder.build().toUriString();
    }
}