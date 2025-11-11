package com.c4.routy.domain.plan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TBL_PLAN")
@Getter
@Setter
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @Column(nullable = false)
    private String planTitle;

    private boolean isPublic;
    private String startDate;
    private String endDate;
    private boolean isDeleted;
    @Column(nullable = false)
    private Integer bookmarkCount = 0; // 기본값 추가
    @Column(nullable = false)
    private Integer viewCount = 0;     // 기본값 추가
    private Integer regionId;
    private Integer userId;

    @Column(updatable = false, insertable = true)
    private String createdAt;
}
