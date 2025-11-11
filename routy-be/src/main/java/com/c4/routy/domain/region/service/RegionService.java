package com.c4.routy.domain.region.service;


import com.c4.routy.domain.region.dto.RegionALLDTO;
import com.c4.routy.domain.region.dto.RegionDTO;
import com.c4.routy.domain.region.entity.RegionEntity;
import com.c4.routy.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;

    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(region -> new RegionDTO(
                        region.getRegionId(),
                        region.getRegionName(),
                        region.getTheme(),
                        region.getRegionDesc()
                ))
                .collect(Collectors.toList());
    }

    // 단건 조회
    public RegionALLDTO getRegionById(Integer regionId) {
        RegionEntity region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("Region not found with id: " + regionId));

        RegionALLDTO dto = new RegionALLDTO();
        dto.setRegionId(region.getRegionId());
        dto.setRegionName(region.getRegionName());
        dto.setAdminCode(region.getAdminCode());
        dto.setTheme(region.getTheme());
        dto.setRegionDesc(region.getRegionDesc());
        dto.setStartLat(region.getStartLat());  // ✅ 중요!
        dto.setStartLng(region.getStartLng());  // ✅ 중요!

        return dto;
    }
}
