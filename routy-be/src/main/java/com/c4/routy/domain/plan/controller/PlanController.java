package com.c4.routy.domain.plan.controller;

import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.service.PlanService;
import com.c4.routy.domain.plan.service.PlanServiceImpl;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;


    // 상세보기 (일정 상세 조회)
    @GetMapping("/{planId}")
    public PlanDetailResponseDTO getPlanDetail(@PathVariable Integer planId) {
        return planService.getPlanDetail(planId);
    }

    // 일정 삭제
    @PatchMapping("/{planId}/delete")
    public ResponseEntity<Void> softDeletePlan(@PathVariable Integer planId) {
        planService.softDeletePlan(planId);
        return ResponseEntity.ok().build();
    }


    // 일정 공개, 비공개처리
    @PatchMapping("/{planId}/public")
    public ResponseEntity<Void> togglePlanPublic(@PathVariable Integer planId) {
        planService.togglePlanPublic(planId);
        return ResponseEntity.ok().build();
    }

    // 필터링(최신순, 조회순, 북마크순, 지역, 날짜,)
    @GetMapping("/public")
    public List<BrowseResponseDTO> getPublicPlans(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(required = false) Integer regionId,
            @RequestParam(required = false) Integer days
    ) {
        List<BrowseResponseDTO> list = planService.getPublicPlans(sort, regionId, days);
        log.info("{}",list);
        return list;
    }


    // 브라우저 상세 모달 보기
    @GetMapping("/public/{planId}")
    public ResponseEntity<BrowseDetailResponseDTO> getPublicPlanDetail(@PathVariable Integer planId) {
        BrowseDetailResponseDTO dto = planService.getPublicPlanDetail(planId);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // 수정화면 로딩
    @GetMapping("/{planId}/edit")
    public PlanEditResponseDTO getPlanEdit(@PathVariable Integer planId) {
        return planService.getPlanEdit(planId);
    }

    // 수정 저장
    @PutMapping("/{planId}")
    public void updatePlan(@PathVariable Integer planId,
                           @RequestBody PlanEditSaveRequestDTO dto) {
        /**
         * 여기서 planId도 프론트에서 받아오고 dto도 프론트에서 받아오는데
         * 굳이 따로 받아서 할 필요가 있나?
         *
         * 하나로 합치는 형식이 좋을 것 같은데...
         */
        dto.setPlanId(planId);
        planService.updatePlan(dto);
    }

    //브라우저 모달 창 좋아요 수 증가 기능
    @PostMapping("/{planId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user) {

        /**
         * 이거 도메인마다 유저 정보를 가져오는 방법이 다르다
         * 동일하게 셋팅을 해야될 것 같은데
         *
         * 1. 토큰을 매개변수로 받아서 해석하기
         * 2. @AuthenticationPrincipal로 토큰에서 유저 정보만 가져오기
         * 3. 서비스 단에서 Authentication객체에서 찾기
         * 4. 그 외가 더 있나?
         *
         * ----------------
         * Gemini 답변
         * 각 방법별 읙녀
         *
         * 1. 토큰을 매개변수로 받아 해석하기(비추천)
         *      - 가장 비효율적
         *      - 중복 코드 발생, 컨트롤러가 보안 로직을 담당하게 됨
         *      - Spring Security.를 쓰는 장점을 버리는 방식
         * 2. 어노테이션을 이용해 컨트롤러에서 받기(추천 - 가장 깔끔함)
         *      - 컨트롤러의 시그니처만 봐도 사용자 정보가 필요함을 알수 있음
         *      - 테스트 코드 작성 시 목객체를 주입하기 편함
         *      * 컨트롤러에서 서비스로 userNo나 user객체를 매번 파라미터로 넘겨야함
         * 3. 서비스 단에서 Authentication 객체에서 찾기(추천 - 편리함)
         *      - 컨트롤러에서 서비스로 파라미터를 넘길 필요가 없어 메서드 시그니처가 깔끔함
         *      - 서비스 내부 어디스던 필요할 때 바로 꺼내 쓸 수 있다.
         *      * 서비스 코드가 Spring Security라는 프레임워크에 의존하게 됨(순수 비즈니스 로직 유지가 어려움)
         *      * 단위 테스트 시 SecurityContext를 Mocking 해줘야 해서 테스트 설정이 조금 번거로울 수 있음
         * 4. 그 외 방법: 커스텀 어노테이션을 만들어 User또는 userNo를 바로 받게 하기
         *      - 사실상 2번 방법을 더 편하게 바꾸는 것
         *
         * 종합 추천: 2번 어노테이션을 이용해 컨트롤러에서 받기
         *      - 컨트롤러가 사용자 정보를 받아서 서비스에 요청한다는 흐름이 자연스러움
         *      - 서비스 계층은 Spring Security를 몰라도 된다(POJO), 순수하게 비즈니스 로직만 테스트하기 좋다
         *
         *      만약 서비스 메서드 파라미터가 많아지는 것이 실다면, 3번도 실무에서 흔하게 쓰이는 방식으로 나쁘지 않다
         *      대신 이를 도와주는 SecurityUtil 같은 헬퍼 클래스를 하나 만들어두고 공통으로 쓰면 코드가 깔끔하게 나옴
         *
         *
         *  ----------------------------
         *  추가 Gemini 질문
         *  Q. Spring Security의 장점과 POJO에 대해 설명해줘
         *  A.
         *      Spring Security의 장점
         *          - 표준화된 보안 로직(검증도니 보안)
         *              - 보안을 직접 구현하면 구멍이 생기기 쉬움. Spring Security는 검증된 방식으로 인증과 인가를 처리
         *              - 세션 고정 공격, CSRF, 클릭재킹 같은 일반적인 웹 보안 공격들을 기본적으로 방어
         *          - 관심사의 분리(AOP)
         *              - 비즈니스 로직(상품주문, 글 쓰기)와 보안 로직(로그인 유무, 관리자/고객 구분)을 완벽하게 분리
         *              - 개발자가 if문이 아닌 어노테이션만을 이용해 보안을 적용 가능
         *          - 확장성과 유연성
         *              - 로그인 방식이 변경되더라도 비즈니스 로직(컨트롤러, 서비스)는 거의 수정할 필요가 없음, 보안 설정 부분만 변경하면 됨
         *              - 다양한 인증 방식(DB, LDAP, 메모리, OAuth2 등)을 표준 인터페이스로 지원
         *          - 편리한 사용자 정보 관리
         *              - SecurityContextHolder를 통해 어디서든 현재 로그인한 사용자 정보에 접근 가능
         *              - 스레드 안전(Thread-safe)하게 관리해주므로 동시성 문제를 신경 쓸 필요가 없음
         *      POJO(Plain Old Java Object)
         *          - 오래된 방식의 평범한 자바 객체라는 뜻으로, 특정 프레임워크나 기술에 종속되지 않은 순수한 자바 객체를 말함
         *          - 포조가 중요한가?
         *              - 테스트 용이성: 특정 환경(톰캣, 스프링 등)을 띄우지 않아도 JUnit 등으로 빠르게 테스트 가능
         *              - 유지보수성: 기술이 바뀌어도 비즈니스 로직 코드는 살아 있음(Spring이 망해도 코드는 안전함)
         *              - 객체지향 설계: 프레임워크의 제약 없이 객체지향적인 설꼐를 자유롭게 할 수 있음
         */

        Integer userId = user.getUserNo(); // 로그인된 사용자 번호

        String message = planService.toggleLike(planId, userId);
        int likeCount = planService.getLikeCount(planId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }

    // 브라우저 지역 드롭 아웃 박스
    @GetMapping("/regions")
    public List<RegionResponseDTO> getAllRegions() {
        return planService.getAllRegions();
    }

    //  조회수 증가 (본인 제외)
    @PostMapping("/{planId}/view")
    public ResponseEntity<Void> increaseViewCount(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer userId = (user != null) ? user.getUserNo() : null;
        planService.increaseViewCount(planId, userId);
        return ResponseEntity.ok().build();
    }

    // 브라우저 북마크 부분
    @PostMapping("/{planId}/bookmark")
    public ResponseEntity<Map<String, Object>> toggleBookmark(
            @PathVariable Integer planId,
            @AuthenticationPrincipal CustomUserDetails user) {

        Integer userId = user.getUserNo();
        String message = planService.toggleBookmark(planId, userId);
        int bookmarkCount = planService.getBookmarkCount(planId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("bookmarkCount", bookmarkCount);

        return ResponseEntity.ok(response);
    }

    // 마이페이지 - 내가 북마크한 일정 목록
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BrowseResponseDTO>> getUserBookmarks(
            @AuthenticationPrincipal CustomUserDetails user) {
        Integer userId = user.getUserNo();
        List<BrowseResponseDTO> bookmarks = planService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    // 브라우저 있는 일정을 내 일정으로 가져오기 기능 (복사 + 날짜 변경 지원)
    @PostMapping("/{planId}/copy")
    public ResponseEntity<Map<String, Object>> copyPlanToMyList(
            @PathVariable Integer planId,
            @RequestBody Map<String, String> dateRange,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer userId = user.getUserNo();


        /**
         * 이거 굳이 여기 있어야 하나?
         * 서비스 단에서 계산하는 것이 맞는 방향일 것 같은데
         */
        // 프론트에서 전달된 날짜 추출
        String startDate = dateRange.get("startDate");
        String endDate = dateRange.get("endDate");

        // 서비스로 전달
        int newPlanId = planService.copyPlanToUser(planId, userId, startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("newPlanId", newPlanId);

        return ResponseEntity.ok(response);
    }
}



