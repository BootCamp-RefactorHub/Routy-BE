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

    /**
     * 가능한 지역 전체 조회
     *
     * 그런데 굳이 stream을 사용해야 했을까?
     *  Mapper에서 이미 resultMap으로 지정해놔서 필요없지 않나?
     *  엔티티로 받고 DTO로 다시 변환하는 과정과 바로 DTO로 받는 것중 어느 것이 더 좋은가
     *  그리고 DB -> 엔티티 -> DTO 시에 modelMapper를 만들어 두고 stream을 이용한 이유는?
     *
     *  예외처리도 고민 필요해보일 듯
     *
     */
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


    /**
     * 지역 단건 조회를 하는 메소드
     *
     * 예외처리는 되어 있으나
     * 객체를 만들고 따로 setter를 사용할 이유가 있나?
     * 처음부터 생성자에 변수를 넣어서 값을 들고 있는 객체로 만드는 방식은?
     * 그리고 modelMapper 사용은?
     * 아니면 Mapper에서 애초에 DTO로 반환 받는 방식은? -> JPA 방식에서 MyBatis로 변호나 필ㅇ
     *
     */
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
        dto.setStartLat(region.getStartLat());  // 중요!
        dto.setStartLng(region.getStartLng());  // 중요!

        return dto;
    }
}
