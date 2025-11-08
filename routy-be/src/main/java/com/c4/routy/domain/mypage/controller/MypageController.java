package com.c4.routy.domain.mypage.controller;


import com.c4.routy.common.util.DateTimeUtil;
import com.c4.routy.domain.mypage.dto.MyPageResponseDTO;
import com.c4.routy.domain.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping
    public MyPageResponseDTO getMyPage(
            @RequestParam Integer userNo,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        // year/month가 없으면 DateTimeUtil로 현재 날짜 계산
        if (year == null || month == null) {
            String today = DateTimeUtil.now(); // 예: "2025-11-06"
            String[] parts = today.split("-"); // [ "2025", "11", "06" ]
            int y = (year != null) ? year : Integer.parseInt(parts[0]);
            int m = (month != null) ? month : Integer.parseInt(parts[1]);
            return mypageService.getMyPage(userNo, y, m);
        }

        // year/month가 넘어온 경우 그대로 사용
        return mypageService.getMyPage(userNo, year, month);
    }
}
