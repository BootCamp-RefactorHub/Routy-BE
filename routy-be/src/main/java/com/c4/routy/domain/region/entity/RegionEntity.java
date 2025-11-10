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
    private Long regionId;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Column(name = "admin_code")
    private Integer adminCode;

    @Column(name = "theme")
    private String theme;

    @Column(name = "region_desc")
    private String regionDesc;
}
