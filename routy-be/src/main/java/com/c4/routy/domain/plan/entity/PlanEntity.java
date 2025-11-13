package com.c4.routy.domain.plan.entity;


import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.region.entity.RegionEntity;
import com.c4.routy.domain.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 여행 일정(플랜) Entity
// TBL_PLAN
// 여행의 최상위 단위 (여행 제목, 기간, 지역 등)


@Entity
@Table(name = "TBL_PLAN")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "plan_title", nullable = false)
    private String planTitle;

    @Column(name = "is_public", nullable = false)
    private boolean shared;

    @Column(name = "start_date", nullable = false)
    private String startDate = DateTimeUtil.now();

    @Column(name = "end_date", nullable = false)
    private String endDate;

    @Column(name= "theme")
    private String theme;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false)
    private String createdAt = DateTimeUtil.now();

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "bookmark_count")
    private Integer bookmarkCount = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private RegionEntity region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DurationEntity> durations = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    /* 편의 메서드 */
    public void changePeriod(String start, String end) {
        this.startDate = start;
        this.endDate = end;
    }
    public void changeTitle(String title) {
        this.planTitle = title;
    }

}
