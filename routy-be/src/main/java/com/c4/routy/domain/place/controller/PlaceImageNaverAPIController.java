//package com.c4.routy.domain.place.controller;
//
//import com.c4.routy.domain.place.service.PlaceImageNaverService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/images")
//@RequiredArgsConstructor
//public class PlaceImageNaverAPIController {
//
//    private final PlaceImageNaverService placeImageNaverService;
//
//    /**
//     * 단일 장소 이미지 검색
//     * GET /api/images/search?query=장소명
//     */
//    @GetMapping("/search")
//    public ResponseEntity<Map<String, String>> searchImage(@RequestParam String query) {
//        log.info("이미지 검색 요청 - 검색어: {}", query);
//
//        String imageUrl = placeImageNaverService.searchImage(query);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("query", query);
//        response.put("imageUrl", imageUrl);
//
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 여러 장소 이미지 일괄 검색
//     * POST /api/images/batch
//     * Body: ["장소1", "장소2", "장소3"]
//     */
//    @PostMapping("/batch")
//    public ResponseEntity<Map<String, String>> searchImagesForPlaces(@RequestBody List<String> placeNames) {
//        log.info("일괄 이미지 검색 요청 - 장소 수: {}", placeNames.size());
//
//        Map<String, String> imageMap = placeImageNaverService.searchImagesForPlaces(placeNames);
//
//        return ResponseEntity.ok(imageMap);
//    }
//}