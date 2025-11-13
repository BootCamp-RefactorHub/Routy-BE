package com.c4.routy.domain.place.controller;

import com.c4.routy.domain.place.service.KakaoCafeService;
import com.c4.routy.domain.place.service.KakaoFoodService;
import com.c4.routy.domain.place.service.KakaoHotelService;
import com.c4.routy.domain.place.service.KakaoAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoPlaceController {

    private final KakaoHotelService kakaoHotelService;
    private final KakaoFoodService kakaoFoodService;
    private final KakaoCafeService kakaoCafeService;
    private final KakaoAttractionService kakaoAttractionService;

    @GetMapping("/hotels")
    public ResponseEntity<String> getNearbyHotels(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = kakaoHotelService.findNearbyHotels(lat ,lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<String> getNearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = kakaoFoodService.findNearbyRestaurants(lat, lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cafes")
    public ResponseEntity<String> getNearbyCafes(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = kakaoCafeService.findNearbyCafes(lat, lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attractions")
    public ResponseEntity<String> getNearbyAttractions(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = kakaoAttractionService.findNearbyAttractions(lat, lng);
        return ResponseEntity.ok(result);
    }
}
