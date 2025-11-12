package com.c4.routy.domain.place.controller;

import com.c4.routy.domain.place.dto.PlaceCreateRequestDTO;
import com.c4.routy.domain.place.dto.PlaceResponseDTO;
import com.c4.routy.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;


    /** 여러 장소 한꺼번에 저장 (배치 저장) */
    @PostMapping("/batch")
    public ResponseEntity<String> addPlaces(@RequestBody List<PlaceCreateRequestDTO> dtoList) {
        placeService.saveAll(dtoList);
        return ResponseEntity.ok(" 장소들이 모두 저장되었습니다!");
    }


    /** 일정에 등록된 장소 조회 */
    @GetMapping("plan/{planId}")
    public ResponseEntity<List<PlaceResponseDTO>> getPlaces(@PathVariable Integer planId) {
        List<PlaceResponseDTO> places = placeService.getPlacesByPlanId(planId);
        return ResponseEntity.ok(places);
    }
}