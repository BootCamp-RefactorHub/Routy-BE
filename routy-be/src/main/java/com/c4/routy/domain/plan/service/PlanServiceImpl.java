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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

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
    @Override
    public List<BrowseResponseDTO> getPublicPlans(int page, int size, String sort, Integer regionId, Integer days) {
        int offset = page * size;
        return planMapper.selectPublicPlans(offset, size, sort, regionId, days);
    }


    //브라우저 카드 일정 상세 조회 (모달용)
    @Override
    public BrowseDetailResponseDTO getPublicPlanDetail(Integer planId) {
        // 기본 정보 및 리뷰
        BrowseDetailResponseDTO dto = planMapper.selectPublicPlanDetail(planId);
        if (dto == null) return null;

        // 리뷰 이미지 문자열 변환
        if (dto.getReview() != null && dto.getReview().getImages() != null) {
            Object imgField = dto.getReview().getImages();
            if (imgField instanceof String imgStr) {
                dto.getReview().setImages(imgStr);
            }
        }

        // Day 및 장소 목록 구성
        List<PlanDayDTO> dayList = planMapper.selectPlanDays(planId);
        for (PlanDayDTO day : dayList) {
            day.setActivities(planMapper.selectPlanPlaces(day.getDayId()));  // ✅ 수정됨
        }
        dto.setDayList(dayList);

        return dto;
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
    public int copyPlanToUser(Integer planId, Integer userId) {
        // 1️⃣ 기존 일정 확인
        PlanDetailResponseDTO original = planMapper.selectPlanDetail(planId);
        if (original == null) {
            throw new IllegalArgumentException("복사할 일정이 존재하지 않습니다. (planId=" + planId + ")");
        }

        // 2️⃣ 새 일정 생성
        PlanCopyDTO copyDTO = new PlanCopyDTO();
        copyDTO.setSourcePlanId(planId);
        copyDTO.setUserId(userId);
        copyDTO.setTitle(original.getTitle() + " (복사본)");
        copyDTO.setStartDate(original.getStartDate());
        copyDTO.setEndDate(original.getEndDate());

        planMapper.insertCopiedPlan(copyDTO);

        // 3️⃣ Day + Travel 복사
        planMapper.copyDurations(planId, copyDTO.getPlanId());
        planMapper.copyTravels(planId, copyDTO.getPlanId());

        return copyDTO.getPlanId();
    }

}
