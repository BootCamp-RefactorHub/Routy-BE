package com.c4.routy.domain.plan.entity;

import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 일정에 대한 리뷰 (TBL_REVIEW)
@Entity
@Table(name = "TBL_REVIEW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at")
    private String createdAt = DateTimeUtil.now();;

    @Column(name = "updated_at")
    private String updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewFileEntity> files = new ArrayList<>();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = DateTimeUtil.now();
    }
}
