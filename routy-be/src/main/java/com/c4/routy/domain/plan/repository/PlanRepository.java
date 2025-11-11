package com.c4.routy.domain.plan.repository;

import com.c4.routy.domain.plan.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 여행 일정(Plan) JPA Repository
// 기본 CRUD
// 사용자별 일정 목록 등 조회용 커스텀 메서드 예시 포함
@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Integer> {
    /** 특정 사용자(userId)의 전체 일정 목록 조회 */
    List<PlanEntity> findByUser_UserNoOrderByStartDateDesc(Integer userNo);

    /** 공개된 플랜만 조회 (둘러보기 페이지 등에서 사용) */
    List<PlanEntity> findBySharedTrueOrderByViewCountDesc();
}
