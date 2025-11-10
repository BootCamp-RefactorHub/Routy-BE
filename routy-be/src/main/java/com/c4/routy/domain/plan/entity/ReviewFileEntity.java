package com.c4.routy.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

//  리뷰에 연결된 이미지 파일 (TBL_REVIEWFILES)
@Entity
@Table(name = "TBL_REVIEWFILES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "original_name")
    private String originalName;

}
