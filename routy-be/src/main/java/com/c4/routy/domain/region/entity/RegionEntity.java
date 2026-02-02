package com.c4.routy.domain.region.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * tbl_region: 여행지 지역과 해당 지역의 위경도 등이 들어있는 테이블
 *
 */

@Entity
@Table(name = "TBL_REGION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "admin_code")
    private String adminCode;

    @Column(name = "theme")
    private String theme;

    @Column(name = "region_desc")
    private String regionDesc;

    @Column(name = "start_lat")
    private Double startLat;

    @Column(name = "start_lng")
    private Double startLng;
}
