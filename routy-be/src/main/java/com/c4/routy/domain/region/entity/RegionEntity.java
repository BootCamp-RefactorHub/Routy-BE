package com.c4.routy.domain.region.entity;


import jakarta.persistence.*;
import lombok.*;

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

    // ✅ 이 두 필드가 있어야 함!
    @Column(name = "start_lat")
    private Double startLat;

    @Column(name = "start_lng")
    private Double startLng;
}
