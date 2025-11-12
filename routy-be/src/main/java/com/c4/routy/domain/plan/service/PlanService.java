package com.c4.routy.domain.plan.service;

import com.c4.routy.domain.plan.dto.*;

import java.util.List;


public interface PlanService {
    PlanDetailResponseDTO getPlanDetail(Integer planId);
    PlanEditResponseDTO getPlanEdit(Integer planId);
    void updatePlan(PlanEditSaveRequestDTO dto);
    int copyPlanToUser(Integer planId, Integer userId,String startDate, String endDate);

    void softDeletePlan(Integer planId);

    void togglePlanPublic(Integer planId);

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
