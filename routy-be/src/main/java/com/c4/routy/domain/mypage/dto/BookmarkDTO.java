package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//마이페이지 북마크
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
    private Integer bookmarkId;     // TBL_BOOKMARK.bookmark_id
    private String createdAt;       // 북마크 추가 한 날짜 TBL_BOOKMARK.created_at
    private Integer planId;         // 일정 번호 TBL_BOOKMARK.plan_id
    private String planTitle;       // 일정 제목 JOIN TBL_PLAN.plan_title

    private Integer bookmarkCount;  // TBL_PLAN.bookmark_count (그 플랜이 받은 북마크 수)
}
