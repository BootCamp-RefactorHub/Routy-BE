package com.c4.routy.domain.region.service;


import com.c4.routy.domain.region.dto.RegionDTO;
import com.c4.routy.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

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
}
