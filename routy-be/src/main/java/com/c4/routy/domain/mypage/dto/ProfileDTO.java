package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//마이페이지 상단 프로필
//프로필 이미지, 사용자 이름, 소개(키워드 선택했던거)
//여행 횟수(일정 수), 작성 리뷰 수, 좋아요 받은 갯수, 북마크 한 일정 수

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Integer userNo;  // TBL_USER.user_no
    private String username; // 회원 이름 TBL_USER.username
    private String profileImage;     // 회원 프로필사진 (없을시 디폴트값) TBL_USER.image

    private int totalPlanCount;      // (총 여행 횟수)내가 만든 일정 수 (is_deleted = 0값만)
    private int totalReviewCount;    // 내가 작성한 리뷰 수
    private int totalLikeReceived;   // 내가 만든 일정들이 받은 좋아요 수
    private int totalBookmarkCount;  // 내가 북마크해둔 일정 수

    // profile 이미지 추가하는 부분
    public void applyDefaultProfile(String defaultUrl) {
        if (this.profileImage == null || this.profileImage.isBlank()) {
            this.profileImage = defaultUrl;
        }
    }
}

