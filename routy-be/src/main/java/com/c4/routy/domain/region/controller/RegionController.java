package com.c4.routy.domain.region.controller;

import com.c4.routy.domain.region.dto.RegionALLDTO;
import com.c4.routy.domain.region.dto.RegionDTO;
import com.c4.routy.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionDTO>> getAllRegions() {
        List<RegionDTO> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionALLDTO> getRegionById(@PathVariable Integer regionId) {
        RegionALLDTO region = regionService.getRegionById(regionId);
        return ResponseEntity.ok(region);
    }
}

