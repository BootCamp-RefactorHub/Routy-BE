package com.c4.routy.domain.plandraw.repository;

import com.c4.routy.domain.plan.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanDrawRepository extends JpaRepository<PlanEntity, Integer> {
    // 사용자별 플랜 조회
    List<PlanEntity> findByUserId(Integer userId);
}
