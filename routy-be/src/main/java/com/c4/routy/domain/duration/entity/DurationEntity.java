package com.c4.routy.domain.duration.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_DURATION")
@Getter
@Setter
public class DurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer durationId;

    @Column(nullable = false)
    private Integer day;  // 1일차, 2일차 등

    @Column(nullable = false)
    private Integer planId;
}
