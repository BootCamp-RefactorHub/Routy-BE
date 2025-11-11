package com.c4.routy.domain.plan.repository;

import com.c4.routy.domain.plan.entity.ReviewEntity;
import com.c4.routy.domain.plan.entity.ReviewFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewFileRepository extends JpaRepository<ReviewFileEntity, Integer> {
    List<ReviewFileEntity> findByReview(ReviewEntity review);

    void deleteByReview(ReviewEntity review);
}
