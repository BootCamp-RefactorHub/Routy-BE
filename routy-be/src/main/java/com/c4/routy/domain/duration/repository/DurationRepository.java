package com.c4.routy.domain.duration.repository;

import com.c4.routy.domain.duration.entity.DurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DurationRepository extends JpaRepository<DurationEntity, Integer> {
    List<DurationEntity> findByPlanId(Integer planId);
}
