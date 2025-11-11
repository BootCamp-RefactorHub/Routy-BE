package com.c4.routy.domain.region.repository;

import com.c4.routy.domain.region.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    RegionEntity findByRegionName(String regionName);
}
