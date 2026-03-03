package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.*;

import java.util.List;


public interface PlanService {
    PlanDetailResponseDTO getPlanDetail(Integer planId);

    PlanEditResponseDTO getPlanEdit(Integer planId);

    // 리펙토링 전
    // void updatePlan(PlanEditSaveRequestDTO dto);

    // 리펙토링 후
    void updatePlan(PlanEditSaveRequestDTO dto, Integer userNo);

    int copyPlanToUser(Integer planId, Integer userId,String startDate, String endDate);

    // 리펙토링 전
    // void softDeletePlan(Integer planId);

    // 리펙토링 후
    void softDeletePlan(Integer planId, Integer userNo);

    // 리펙토링 전
    // void togglePlanPublic(Integer planId);

    //리펙토링 후
    void togglePlanPublic(Integer planId, Integer userNo);

    List<BrowseResponseDTO> getPublicPlans(String sort, Integer regionId, Integer days);

    BrowseDetailResponseDTO getPublicPlanDetail(Integer planId);

    String toggleLike(Integer planId, Integer userId);

    int getLikeCount(Integer planId);

    List<RegionResponseDTO> getAllRegions();

    void increaseViewCount(Integer planId, Integer userId);

    String toggleBookmark(Integer planId, Integer userId);

    int getBookmarkCount(Integer planId);

    List<BrowseResponseDTO> getUserBookmarks(Integer userId);

}
