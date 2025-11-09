package com.c4.routy.domain.mypage.service;

import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.mypage.dto.*;
import com.c4.routy.domain.mypage.mapper.MypageQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final MypageQueryMapper mypageQueryMapper;


    @Value("${app.default-profile-image}")
    private String defaultProfileImage;



    public MyPageResponseDTO getMyPage(Integer userNo, int year, int month) {

        // 1) 달력 범위 계산
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end   = start.withDayOfMonth(start.lengthOfMonth());

        String startStr = start.toString(); // yyyy-MM-dd
        String endStr   = end.toString();   // yyyy-MM-dd
        String today    = LocalDate.now().toString();

        // 2) 각 영역별 조회
        ProfileDTO profile = mypageQueryMapper.selectProfile(userNo);

        // 🔹 프로필이 없거나 이미지가 비어있을 경우, 기본 이미지 적용
        if (profile == null) {
            profile = new ProfileDTO();
            profile.setProfileImage(defaultProfileImage);
        } else if (profile.getProfileImage() == null || profile.getProfileImage().isEmpty()) {
            profile.setProfileImage(defaultProfileImage);
        }

        List<CalendarPlanDTO> calendarPlans =
                mypageQueryMapper.selectCalendarPlans(userNo, startStr, endStr);
        List<MyPlanDTO> upcomingPlans =
                mypageQueryMapper.selectUpcomingPlans(userNo, today);
        List<TravelRecordDTO> travelHistory =
                mypageQueryMapper.selectTravelHistory(userNo);
        List<BookmarkDTO> bookmarks =
                mypageQueryMapper.selectBookmarks(userNo);

        // 3) 달력 DTO 조립
        CalendarDTO calendarDTO = CalendarDTO.builder()
                .year(year)
                .month(month)
                .plans(calendarPlans)
                .build();

        // 4) 화면에서 필요한 가공값 채우기
        upcomingPlans.forEach(plan -> {
            // 기간 라벨
            LocalDate s = LocalDate.parse(plan.getStartDate());
            LocalDate e = LocalDate.parse(plan.getEndDate());
            int days = (int) (e.toEpochDay() - s.toEpochDay()) + 1;
            // 1박2일 이런 식으로 맞추고 싶으면 여기서 규칙 바꾸면 됨
            plan.setDurationLabel(days + "일 일정");

            // 상태값: 예정/진행중/완료
            LocalDate now = LocalDate.now();
            if (now.isBefore(s)) {
                plan.setStatus("예정");
            } else if ((now.isEqual(s) || now.isAfter(s)) && now.isBefore(e.plusDays(1))) {
                plan.setStatus("진행중");
            } else {
                plan.setStatus("완료");
            }
        });

        // 5) 최상위 DTO로 묶어서 반환
        return MyPageResponseDTO.builder()
                .profile(profile)
                .calendar(calendarDTO)
                .upcomingPlans(upcomingPlans)
                .travelHistory(travelHistory)
                .bookmarks(bookmarks)
                .build();
    }
}
