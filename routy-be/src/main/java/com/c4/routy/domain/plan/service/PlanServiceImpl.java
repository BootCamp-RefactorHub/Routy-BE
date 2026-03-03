package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.duration.entity.DurationEntity;
import com.c4.routy.domain.duration.repository.DurationRepository;
import com.c4.routy.domain.plan.dto.*;
import com.c4.routy.domain.plan.entity.PlanEntity;
import com.c4.routy.domain.plan.entity.TravelEntity;
import com.c4.routy.domain.plan.mapper.PlanMapper;
import com.c4.routy.domain.plan.repository.PlanRepository;
import com.c4.routy.domain.plan.repository.TravelRepository;
import com.c4.routy.domain.region.entity.RegionEntity;
import com.c4.routy.domain.region.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    /**
     * 이거 CQRS 구조로 변경은 어떠한가?
     * 
     * 그냥 조회를 별도 클래스로 분리해서 관리해주는 방향은?
     * 
     * -----
     * Gemini 답변
     * 
     * 결론부터 말씀드리면: 현재 프로젝트 규모와 복잡도를 고려했을 때, 완전한 CQRS(DB 분리 등)보다는 
     * "코드 레벨에서의 분리(단순 조회용 서비스 분리)" 정도가 적절하고 효율적일 것으로 보입니다.
     * 
     * 장점
     *  - 복잡도 관리: 비즈니스 로직은 명령쪽에 몰려 있고, 조회는 단순 데이터 조합인 경우가 많음. 이를 분리해주면 코드가 훨씬 깔끔해진다.
     *  - 최적화 용이: 조회 로직은 JPA의 Dirty Checking이나 트랜잭션 오버헤드 없기 일기 전용 트랜잭션으로 최적화하거나, 필요하면 MyBatis로 복잡한 쿼리만 따로 짤 수 있음
     *  - 가독성: 클래스 분리를 통해 각 클래스의 역할을 바로 알 수 있음
     * 단점
     *  - 클래스 폭발: 파일 개수가 2배로 늘어남, 간단한 CRUD만 있는 도메인이라면 Over-engineering이 될 수 있음
     *  - 러닝 커브: 팀원들이 익숙하지 않으면 "왜 굳이 나눴지?"하고 혼란스로울 수 있음
     *  
     * 추천 방향
     *  - 거창하게 DB를 나누거나 메시지 큐를 도입하는 CQRS가 아니라, 클래스 설계 레벨에서의 분리를 추천
     *  
     * 결론 및 제안
     *  - 복잡한 도메인만 분리: 모든 도메인을 다 나누지 말고, 로직이 복잡하고 메서드가 많은 핵심 도메인부터 적용
     *  - 조회 성능 최적화: 조회 전용  서비스에서는 엔티티를 직접 리턴하기 보다 DTO로 바로 조회하는 방식을 쓰면 성능 이점도 챙길 수 있음
     *  - 유지보수성: 이렇게 나누면 나중에 "조회 기능이 너무 느려서 캐시를 붙여야겠다" 또는 "검색 엔진을 도입해야겠다" 할 때 QueryService만 건드리면 되므로 유지보수가 빨라짐
     *  
     * 한 줄 요약
     *  - 완전한 아키텍쳐 변경보다는, 서비스 클래스가 너무 커지는 것을 막기 위해 CommandService와 QueryService로 쪼개는 것부터 시작하는 것을 추천
     */
    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final DurationRepository DurationRepository;
    private final TravelRepository travelRepository;
    private final RegionRepository RegionRepository;


    /**
     * 상세 조회
     * - mapper에서 기본 정보 가져오고
     * - 다른 브랜치에서 하던 일수/박수/상태 계산까지 여기서 처리
     */
    @Override
    public PlanDetailResponseDTO getPlanDetail(Integer planId) {
        PlanDetailResponseDTO dto = planMapper.selectPlanDetail(planId);

        if (dto == null) {
            throw new IllegalArgumentException("해당 일정의 상세 데이터가 없습니다. (planId=" + planId + ")");
        }

        // startDate / endDate 는 mapper에서 문자열로 온다고 가정 (yyyy-MM-dd)
        LocalDate start = LocalDate.parse(dto.getStartDate());
        LocalDate end = LocalDate.parse(dto.getEndDate());

        // 몇일짜리 여행인지
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
        dto.setDays(days);
        dto.setNights(days - 1);

        // 상태값 계산
        LocalDate today = LocalDate.now();
        if (today.isBefore(start)) {
            dto.setStatus("진행예정");
        } else if (today.isAfter(end)) {
            dto.setStatus("완료");
        } else {
            dto.setStatus("진행중");
        }

        /**
         * 지금 상세 조회기능이 본인 일정을 본다는 가정하에 있는 것인가?
         * 남의 일정을 보는 것은 따로 있나? 그게 아니면 저게 무조건 true이면 안될텐데?
         * 만일 따로 있다면 통합하고 user 정보를 받아오게 해서 조절하는게 맞을 듯
         *
         */
        // 프론트에서 쓸 플래그 기본값
        dto.setEditable(true);
        dto.setReviewWritable(true);

        return dto;
    }

    @Override
    public PlanEditResponseDTO getPlanEdit(Integer planId) {
        return planMapper.selectPlanEdit(planId);
    }

    /**
     * 일정 수정 저장 로직
     * - 기존 Day/Travel 싹 지우고 새로 INSERT 하는 방식 (가장 단순, 프론트 구조랑 1:1 매칭)
     */
    @Override
    @Transactional
    public void updatePlan(PlanEditSaveRequestDTO dto) {

        PlanEntity plan = planRepository.findById(dto.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("플랜을 찾을 수 없습니다."));

        // 제목/기간 변경
        plan.changeTitle(dto.getTitle());
//        plan.changePeriod(dto.getStartDate(), dto.getEndDate());
        if (dto.getStartDate() != null && dto.getEndDate() != null
                && !dto.getStartDate().isBlank() && !dto.getEndDate().isBlank()) {
            plan.changePeriod(dto.getStartDate(), dto.getEndDate());
        }
        // 지역 변경
        if (dto.getDestination() != null) {
            RegionEntity region = RegionRepository.findByRegionName(dto.getDestination());
            plan.setRegion(region);
        }

        // 기존 Day/Travel 다 삭제
        List<DurationEntity> oldDays = DurationRepository.findByPlan(plan);
        for (DurationEntity oldDay : oldDays) {
            travelRepository.deleteByDuration(oldDay);
        }
        DurationRepository.deleteByPlan(plan);

        // 새로 insert
        for (PlanEditSaveDayDTO dayDTO : dto.getDayList()) {
            DurationEntity dayEntity = DurationEntity.builder()
                    .plan(plan)
                    .day(dayDTO.getDayNo())
                    .build();
            DurationRepository.save(dayEntity);

            for (PlanEditSaveActivityDTO actDTO : dayDTO.getActivities()) {
                TravelEntity travel = TravelEntity.builder()
                        .duration(dayEntity)
                        .placeName(actDTO.getPlaceName())
                        .addressName(actDTO.getAddressName())
                        .categoryGroupName(actDTO.getCategoryGroupName())
                        .placeUrl(actDTO.getPlaceUrl())
                        .startTime(actDTO.getStartTime())    // 추가
                        .endTime(actDTO.getEndTime())        // 추가
//                        .latitude(actDTO.getLatitude())      // 추가 (있으면)
//                        .longitude(actDTO.getLongitude())    // 추가 (있으면)
//                        .categoryCode(actDTO.getCategoryCode()) // 추가 (있으면)
//                        .tag(actDTO.getTag())
                        .travelOrder(actDTO.getTravelOrder())  // 엔티티에 맞게 수정
                        .build();
                travelRepository.save(travel);
            }
        }

        planRepository.save(plan);
    }
    // 게시글 소프트 삭제 기능
    @Override
    public void softDeletePlan(Integer planId) {
        planMapper.softDeletePlan(planId);
    }

    // 공유하기 기능
    @Override
    public void togglePlanPublic(Integer planId) {
        planMapper.togglePlanPublic(planId);
    }

    // 헤더 부분에 있는 여행 루트 둘러러보기
    @Value("${app.default-plan-image}")
    private String defaultPlanImage;

    @Override
    public List<BrowseResponseDTO> getPublicPlans(String sort, Integer regionId, Integer days) {

        List<BrowseResponseDTO> list = planMapper.selectPublicPlans(days, regionId, sort);

        for (BrowseResponseDTO dto : list) {

            // 1) 리뷰 이미지 콤마 분리
            if (dto.getReviewImagesRaw() != null && !dto.getReviewImagesRaw().isEmpty()) {
                dto.setReviewImages(Arrays.asList(dto.getReviewImagesRaw().split(",")));
            } else {
                dto.setReviewImages(List.of());  // 빈 리스트로 초기화
            }

            // 2) 리뷰 이미지가 아예 없는 경우 → 기본 이미지 1개 삽입
            if (dto.getReviewImages().isEmpty()) {
                dto.setReviewImages(List.of(dto.getDefaultPlanImage()));
            }
        }
        log.info("fltmxmdhskfhdskh {}",list);
        return list;
    }



    //브라우저 카드 일정 상세 조회 (모달용)
    @Override
    public BrowseDetailResponseDTO getPublicPlanDetail(Integer planId) {

        // 1) 기본 상세 조회 (plan 정보 + days)
        BrowseDetailResponseDTO detail = planMapper.selectPublicPlanDetail(planId);
        if (detail == null) {
            throw new IllegalArgumentException("해당 일정이 존재하지 않습니다. planId=" + planId);
        }

        // 2) 리뷰 + 리뷰 이미지 조회 (별도 쿼리)
        BrowseReviewModalDTO review = planMapper.selectPlanReview(planId);

        if (review != null) {
            // 3) 이미지 파싱 (콤마 문자열 → 리스트)
            if (review.getImages() != null && !review.getImages().isBlank()) {
                String[] split = review.getImages().split(",");
                // 공백 제거 + 빈 문자열 제거까지 해주면 안전
                List<String> imageList = Arrays.stream(split)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                review.setImageList(imageList);
            } else {
                review.setImageList(Collections.emptyList());
            }

            // 4) 최종 상세 객체에 review 세팅
            detail.setReview(review);
        } else {
            // 리뷰가 아예 없는 경우
            detail.setReview(null);
        }

        return detail;
    }



    // 브라우저 모달 창 좋아요 토글
    @Override
    public String toggleLike(Integer planId, Integer userId) {
        // 작성자 ID 확인
        Integer authorId = planMapper.selectPlanAuthorId(planId);
        if (authorId != null && authorId.equals(userId)) {
            throw new IllegalArgumentException("본인은 자신의 글에 좋아요를 누를 수 없습니다.");
        }

        // 좋아요 상태 확인 후 토글
        boolean exists = planMapper.checkUserLike(planId, userId);

        if (exists) {
            planMapper.deleteLike(planId, userId);
            return "좋아요 취소";
        } else {
            planMapper.insertLike(planId, userId);
            return "좋아요 추가";
        }
    }


    // 좋아요 개수 조회
    @Override
    public int getLikeCount(Integer planId) {
        return planMapper.countLikes(planId);
    }

    public List<RegionResponseDTO> getAllRegions() {
        return planMapper.selectAllRegions();
    }

    public void increaseViewCount(Integer planId, Integer userId) {
        Integer authorId = planMapper.selectPlanAuthorId(planId);

        // 비로그인 상태나 본인일 경우 카운트 증가 X
        if (userId == null || authorId.equals(userId)) {
            return;
        }

        planMapper.incrementViewCount(planId);
    }


    // 브라우저 북마크 관련 부분
    @Override
    public String toggleBookmark(Integer planId, Integer userId) {
        boolean exists = planMapper.checkUserBookmark(planId, userId);

        if (exists) {
            planMapper.deleteBookmark(planId, userId);
        } else {
            planMapper.insertBookmark(planId, userId);
        }

        // ✅ 북마크 개수 tbl_plan에 반영
        planMapper.updateBookmarkCount(planId);

        return exists ? "북마크 취소" : "북마크 추가";
    }


    // 북마크 개수 가져오기
    @Override
    public int getBookmarkCount(Integer planId) {
        return planMapper.countBookmarks(planId);
    }

    public List<BrowseResponseDTO> getUserBookmarks(Integer userId) {
        return planMapper.selectUserBookmarks(userId);
    }

    @Override
    public int copyPlanToUser(Integer planId, Integer userId, String startDate, String endDate) {
        // 기존 일정 확인
        PlanDetailResponseDTO original = planMapper.selectPlanDetail(planId);
        if (original == null) {
            throw new IllegalArgumentException("복사할 일정이 존재하지 않습니다. (planId=" + planId + ")");
        }

        // 새 일정 생성
        PlanCopyDTO copyDTO = new PlanCopyDTO();
        copyDTO.setSourcePlanId(planId);
        copyDTO.setUserId(userId);
        copyDTO.setTitle(original.getTitle() + " (복사본)");

        // 날짜 선택 반영 (프론트에서 받은 값이 있으면 그걸 사용)
        if (startDate != null && !startDate.isBlank()) {
            copyDTO.setStartDate(startDate);
        } else {
            copyDTO.setStartDate(original.getStartDate());
        }

        if (endDate != null && !endDate.isBlank()) {
            copyDTO.setEndDate(endDate);
        } else {
            copyDTO.setEndDate(original.getEndDate());
        }

        // DB에 새 일정 삽입
        planMapper.insertCopiedPlan(copyDTO);

        //  Day + Travel 복사 (기존 일정 구조 유지)
        planMapper.copyDurations(planId, copyDTO.getPlanId());
        planMapper.copyTravels(planId, copyDTO.getPlanId());

        return copyDTO.getPlanId();
    }


}
