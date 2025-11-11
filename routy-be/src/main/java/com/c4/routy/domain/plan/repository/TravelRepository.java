package com.c4.routy.domain.plan.repository;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.plan.entity.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<TravelEntity, Integer> {
    List<TravelEntity> findByDuration(DurationEntity duration);

    void deleteByDuration(DurationEntity duration);
}
