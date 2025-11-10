package com.c4.routy.domain.place.controller;

import com.c4.routy.domain.place.service.PlaceKakaoCafeService;
import com.c4.routy.domain.place.service.PlaceKakaoFoodService;
import com.c4.routy.domain.place.service.PlaceKakaoHotelService;
import com.c4.routy.domain.place.service.PlaceKakaoAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class PlaceKakaoAPIController {

    private final PlaceKakaoHotelService placeKakaoHotelService;
    private final PlaceKakaoFoodService placeKakaoFoodService;
    private final PlaceKakaoCafeService placeKakaoCafeService;
    private final PlaceKakaoAttractionService placeKakaoAttractionService;

    @GetMapping("/hotels")
    public ResponseEntity<String> getNearbyHotels(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = placeKakaoHotelService.findNearbyHotels(lat ,lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<String> getNearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = placeKakaoFoodService.findNearbyRestaurants(lat, lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cafes")
    public ResponseEntity<String> getNearbyCafes(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = placeKakaoCafeService.findNearbyCafes(lat, lng);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attractions")
    public ResponseEntity<String> getNearbyAttractions(
            @RequestParam double lat,
            @RequestParam double lng) {

        String result = placeKakaoAttractionService.findNearbyAttractions(lat, lng);
        return ResponseEntity.ok(result);
    }
}
