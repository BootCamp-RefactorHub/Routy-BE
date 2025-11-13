package com.c4.routy.domain.place.controller;

import com.c4.routy.domain.place.dto.KakaoPlaceResponse;
import com.c4.routy.domain.place.dto.RestaurantSearchRequest;
import com.c4.routy.domain.place.service.KakaoPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final KakaoPlaceService kakaoPlaceService;

    /**
     * 키워드로 맛집 검색
     * GET /api/restaurants/search?query=파스타&page=1&size=15
     */
    @GetMapping("/search")
    public ResponseEntity<KakaoPlaceResponse> searchRestaurants(
            @RequestParam String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(required = false) Integer radius,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "15") Integer size
    ) {
        log.info("맛집 검색 요청 - query: {}, page: {}, size: {}", query, page, size);

        RestaurantSearchRequest request = new RestaurantSearchRequest();
        request.setQuery(query);
        request.setCategory(category);
        request.setX(x);
        request.setY(y);
        request.setRadius(radius);
        request.setPage(page);
        request.setSize(size);

        KakaoPlaceResponse response = kakaoPlaceService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 음식점 카테고리로 맛집 검색
     * POST /api/restaurants/search/category
     */
    @PostMapping("/search/category")
    public ResponseEntity<KakaoPlaceResponse> searchRestaurantsByCategory(
            @RequestBody RestaurantSearchRequest request
    ) {
        log.info("카테고리 맛집 검색 요청 - query: {}", request.getQuery());

        KakaoPlaceResponse response = kakaoPlaceService.searchRestaurantsByCategory(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 위치 기반 주변 맛집 검색
     * GET /api/restaurants/nearby?x=127.123&y=37.456&radius=3000
     */
    @GetMapping("/nearby")
    public ResponseEntity<KakaoPlaceResponse> searchNearbyRestaurants(
            @RequestParam String x,
            @RequestParam String y,
            @RequestParam(required = false, defaultValue = "5000") Integer radius,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "15") Integer size
    ) {
        log.info("주변 맛집 검색 요청 - x: {}, y: {}, radius: {}m", x, y, radius);

        KakaoPlaceResponse response = kakaoPlaceService.searchNearbyRestaurants(x, y, radius, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 키워드 + 위치로 맛집 검색
     * POST /api/restaurants/search/location
     */
    @PostMapping("/search/location")
    public ResponseEntity<KakaoPlaceResponse> searchRestaurantsByLocation(
            @RequestBody RestaurantSearchRequest request
    ) {
        log.info("위치 기반 맛집 검색 - query: {}, x: {}, y: {}",
                request.getQuery(), request.getX(), request.getY());

        KakaoPlaceResponse response = kakaoPlaceService.searchRestaurants(request);
        return ResponseEntity.ok(response);
    }
}