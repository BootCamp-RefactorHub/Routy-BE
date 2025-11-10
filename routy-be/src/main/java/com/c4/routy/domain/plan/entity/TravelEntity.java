package com.c4.routy.domain.plan.entity;

import com.c4.routy.domain.duration.entity.DurationEntity;
import jakarta.persistence.*;
import lombok.*;


// 하루 일정 내의 개별 여행 포인트 (TBL_TRAVEL)
// Duration과 1:N 관계
@Entity
@Table(name = "TBL_TRAVEL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Integer travelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duration_id")
    private DurationEntity duration;

    @Column(name = "place_name")
    private String placeName;           // 장소이름

    @Column(name = "address_name")       //주소
    private String addressName;

    @Column(name = "category_group_name")
    private String categoryGroupName;

    @Column(name = "place_url")       // 장소url
    private String placeUrl;

    @Column(name = "tag")
    private String tag;                 // 숙소/식당/관광...

    @Column(name = "travel_order")
    private Integer travelOrder;
}
