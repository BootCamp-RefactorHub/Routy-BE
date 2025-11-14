package com.c4.routy.domain.place.controller;

import com.c4.routy.domain.place.dto.KakaoPlaceResponse;
import com.c4.routy.domain.place.dto.KakaoSearchRequest;
import com.c4.routy.domain.place.service.KakaoSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/kakao")
@RequiredArgsConstructor
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    /**
     * 지역명으로 맛집 검색
     * GET /api/restaurants/search?query=대구
     * → "대구 맛집"으로 자동 검색, 상위 5개 반환
     */
    @GetMapping("/restaurants/search")
    public ResponseEntity<KakaoPlaceResponse> searchRestaurants(
            @RequestParam String query
    ) {
        String searchQuery = query + " 맛집";
        log.info("맛집 검색 - 원본: {}, 검색어: {}", query, searchQuery);

        KakaoSearchRequest request = new KakaoSearchRequest();
        request.setQuery(searchQuery);
        request.setCategory("FD6");
        request.setPage(1);
        request.setSize(5);

        KakaoPlaceResponse response = kakaoSearchService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 지역명으로 카페 검색
     * GET /api/cafes/search?query=대구
     * → "대구 카페"로 자동 검색, 상위 5개 반환
     */
    @GetMapping("/cafes/search")
    public ResponseEntity<KakaoPlaceResponse> searchCafes(
            @RequestParam String query
    ) {
        String searchQuery = query + " 카페";
        log.info("카페 검색 - 원본: {}, 검색어: {}", query, searchQuery);

        KakaoSearchRequest request = new KakaoSearchRequest();
        request.setQuery(searchQuery);
        request.setCategory("CE7");
        request.setPage(1);
        request.setSize(5);

        KakaoPlaceResponse response = kakaoSearchService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 지역명으로 관광지 검색
     * GET /api/attractions/search?query=대구
     * → "대구 관광지"로 자동 검색, 상위 5개 반환
     */
    @GetMapping("/attractions/search")
    public ResponseEntity<KakaoPlaceResponse> searchAttractions(
            @RequestParam String query
    ) {
        String searchQuery = query + " 관광지";
        log.info("관광지 검색 - 원본: {}, 검색어: {}", query, searchQuery);

        KakaoSearchRequest request = new KakaoSearchRequest();
        request.setQuery(searchQuery);
        request.setCategory("AT4");
        request.setPage(1);
        request.setSize(5);

        KakaoPlaceResponse response = kakaoSearchService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 테마별 추천 장소 검색
     * GET /api/kakao/theme-search?query=대구&theme=restaurant
     * → 테마에 맞는 키워드로 자동 변환하여 검색
     */
    @GetMapping("/theme-search")
    public ResponseEntity<KakaoPlaceResponse> searchByTheme(
            @RequestParam String query,
            @RequestParam String theme
    ) {
        String searchQuery = query + " ";
        String category = "";

        switch (theme) {
            case "restaurant":
                searchQuery += "맛집";
                category = "FD6";
                break;
            case "cafe":
                searchQuery += "카페";
                category = "CE7";
                break;
            case "tourist":
                searchQuery += "관광지";
                category = "AT4";
                break;
            default:
                searchQuery += "맛집";
                category = "FD6";
        }

        log.info("테마 검색 - 테마: {}, 지역: {}, 검색어: {}", theme, query, searchQuery);

        KakaoSearchRequest request = new KakaoSearchRequest();
        request.setQuery(searchQuery);
        request.setCategory(category);
        request.setPage(1);
        request.setSize(5);

        KakaoPlaceResponse response = kakaoSearchService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/keyword-search")
    public ResponseEntity<?> keywordSearch(
            @RequestParam String query,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng
    ) {
        // 서비스에서 Kakao Local API 호출
        return ResponseEntity.ok(
                kakaoSearchService.searchByKeyword(query, lat, lng)
        );
    }
}