package com.c4.routy.domain.mypage.mapper;

import com.c4.routy.domain.mypage.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageQueryMapper {

    // 1) 상단 프로필
    ProfileDTO selectProfile(@Param("userNo") Integer userNo);

    // 2) 달력에 뿌릴 일정 (해당 월)
    List<CalendarPlanDTO> selectCalendarPlans(
            @Param("userNo") Integer userNo,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    // 3) 오른쪽 "내 일정" - 다가오는 일정
    List<MyPlanDTO> selectUpcomingPlans(
            @Param("userNo") Integer userNo,
            @Param("today") String today
    );

    // 4) 여행 기록 (지난 일정들, 3~4개만)
    List<TravelRecordDTO> selectTravelHistory(
            @Param("userNo") Integer userNo
    );

    // 5) 북마크 목록
    List<BookmarkDTO> selectBookmarks(
            @Param("userNo") Integer userNo
    );
}
