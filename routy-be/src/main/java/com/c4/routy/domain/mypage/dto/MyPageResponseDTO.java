package com.c4.routy.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


//최상위 요청 DTO
//한 화면에 필요한 여러 데이터를 한 번에 묶어서 전달하기 위한용도(API 응답용)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDTO {

    // 상단 프로필 영역
    private ProfileDTO profile;

    // 여행 달력 영역
    private CalendarDTO calendar;

    // 오른쪽 "내 일정" 영역 (다가오는 일정)
    private List<MyPlanDTO> upcomingPlans;

    // 여행 기록 영역 (내가 다녀온 일정 카드)
    private List<TravelRecordDTO> travelHistory;

    // 북마크 영역
    private List<BookmarkDTO> bookmarks;
}
