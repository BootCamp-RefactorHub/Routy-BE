package com.c4.routy.domain.mypage.dto;

import lombok.Builder;
import lombok.Data;

//마이페이지 상단 프로필
//프로필 이미지, 사용자 이름, 소개(키워드 선택했던거), 여행 횟수, 작성 리뷰

@Data
@Builder
public class ProfileDTO {

    private Integer userNo;  // TBL_USER.user_no
    private String username; // TBL_USER.username
    private String email;    // TBL_USER.email
    private String profileImage;     // TBL_USER.image

    private int totalPlanCount;      // SELECT COUNT(*) FROM TBL_PLAN WHERE user_id = :userNo AND is_deleted = 0
    private int totalReviewCount;    // SELECT COUNT(*) FROM TBL_REVIEW r JOIN TBL_PLAN p ON r.plan_id = p.plan_id WHERE p.user_id = :userNo
}

