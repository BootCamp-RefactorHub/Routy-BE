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
    @Column(name = "reviewfile_id")
    private Integer reviewfileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_rename")
    private String fileRename;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

}
