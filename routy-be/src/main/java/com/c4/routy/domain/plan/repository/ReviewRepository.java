package com.c4.routy.domain.plan.repository;

import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.entity.ReviewEntity;
import com.c4.routy.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    // 플랜에 대한 리뷰 1개만 허용할 때
    Optional<ReviewEntity> findByPlan(PlanEntity plan);

    // 유저별 리뷰
    Optional<ReviewEntity> findByPlanAndUser(PlanEntity plan, UserEntity user);
}
