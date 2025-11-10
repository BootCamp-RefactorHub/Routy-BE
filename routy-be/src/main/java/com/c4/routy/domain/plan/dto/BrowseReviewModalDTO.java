package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


// 헤더 위에 여행 루트 둘러보기에서 카드 클릭시 나오는 모달창에 쓰이는 DTO(BrowseDetailResponseDTO에 쓰임)
// 추후 마이페이지에서 일정 들어가 쓰는 리뷰 작성하기 DTO와 맞춰야 함

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowseReviewModalDTO {
    private Integer reviewId;
    private String content;    // 리뷰 내용
    private String createdAt;  // 작성일
    private Integer rating;    // 별점
    private String username;   // 작성자 이름
    private String images;
}
