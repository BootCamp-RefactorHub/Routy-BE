package com.c4.routy.domain.mypage.controller;

import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.mypage.dto.BookmarkDTO;
import com.c4.routy.domain.mypage.dto.MyPageResponseDTO;
import com.c4.routy.domain.mypage.dto.TravelRecordDTO;
import com.c4.routy.domain.mypage.service.MypageService;
import com.c4.routy.domain.plan.dto.BrowseDetailResponseDTO;
import com.c4.routy.domain.plan.service.PlanService;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final PlanService planService;

    @GetMapping
    public MyPageResponseDTO getMyPage(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Authentication authentication
    ) {
        // 인증된 사용자 정보에서 userNo 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userNo = userDetails.getUserNo();

        // year/month가 없으면 오늘 날짜 기준 계산
        if (year == null || month == null) {
            String today = DateTimeUtil.now(); // 예: "2025-11-06"
            String[] parts = today.split("-");
            int y = (year != null) ? year : Integer.parseInt(parts[0]);
            int m = (month != null) ? month : Integer.parseInt(parts[1]);
            return mypageService.getMyPage(userNo, y, m);
        }

        return mypageService.getMyPage(userNo, year, month);
    }


//    전체 여행 기록 (월 제한 없음)

    @GetMapping("/travel-history")
    public List<TravelRecordDTO> getAllTravelRecords(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userNo = userDetails.getUserNo();

        return mypageService.getAllTravelRecords(userNo);
    }


//     전체 북마크 (월 제한 없음)
    @GetMapping("/bookmarks")
    public List<BookmarkDTO> getAllBookmarks(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userNo = userDetails.getUserNo();

        return mypageService.getAllBookmarks(userNo);
    }




    // 북마크 상세 보기
    @GetMapping("/bookmark/public/{planId}")
    public ResponseEntity<?> getPublicPlanDetail(
            @PathVariable Integer planId
    ) {
        try {
            BrowseDetailResponseDTO dto = planService.getPublicPlanDetail(planId);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("일정 상세 조회 실패: " + e.getMessage());
        }
    }
}



