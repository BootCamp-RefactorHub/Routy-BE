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
        /**
         * plandraw랑 여기랑 둘다 동근님이 작성한 부분인데 토큰에서 인증정보를 가져오는 방법이 다름
         * 하나로 맞춰서 해도 좋을 듯
         *
         * 날짜 계산 부분도 service로 넘기는 것도 생각좀 해봐야할듯
         */
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


    /**
     * 해당함수는 planService를 사용하는 것으로 보아 Plan 도메인으로 가는게 맞지 않나?
     *
     * 북마크 보는게 mypage에서 보는게 맞기 한데 확실히 해야할듯
     * Service 단을 mypage쪽으로 옮기거나 이 컨트롤러를 plan 도메인으로 옮기든가
     *
     * 보니까 이미 있는 기능을 재사용하느라 그런 것 같은데... 음.... 어떻게 하는 것이 좋을까?
     * -----------------------------------------------------------------------------
     * Gemini 에게 질문
     *  Q. 현재 상황이 메서드의 재사용을 통해 코드의 줄 수를 줄인 것으로 보이는데 어느 방향으로 가는게 맞나?
     *      1. 도메인의 확실한 분리
     *      2. 코드 재사용
     *      3. 그 외
     *  A.
     *      단점
     *       북마크된 일정도 본질적으로 일정(plan)입니다. 마이페이지에서 접근했다고 해서 그 데이터의 본질이 마이페이지 데이터로 변하는 것은 아니다.
     *      이로 인해 리소스 정체성 혼란이라는 단점이 생긴다.
     *
     *      해결책
     *       일반적으로 3. 그 외(RESTful 리소스 중심 설계) 방식으로 해결한다.
     *       해당 API를 제거하고, 프론트 엔드에서 PlanController에 있는 엔드 포인트를 호출하게 변경
     *
     *      이유
     *       리소스의 일관성: 사용자가 검색을 통해 들어오든, 마이페이지 북마크를 통해 들어오든 일정 상세 정보를 보는 것은 동일 행위이기 때문에 동일한 API를 사용하는 것이 맞다
     *       도메인 분리: MypageController는 MypageService만 바라보고, PlanController는 PlanService만 바라보게 하여 의존성을 말끔하게 유지할 수 있다.
     *
     *      예외 사항
     *       단순 조회가 아닌 마이페이지에서만 보여줘야 하는 특수한 데이터가 섞여 있다면 달라짐
     *       EX) "일정 상세 정보 + 나의 메모 + 나의 방문 횟수"를 같이 줘야 한다면 Facade 패턴 또는 Service 간의 협력이 필요
     *        이 경우 MypageService 내부에서 PlanService(혹은 Repository)를 호출하여 데이터를 가져온 뒤, 마이페이지 전용 DTO로 가공해서 리턴
     *        하지만 이 경우에도 Controller가 다른 도메인의 Service를 직접 호출하기보다는 MypageService가 그 역할을 위임받아 처리하는 것이 계층 구조상 더 깔
     */
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



