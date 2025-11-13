package com.c4.routy.domain.mypage.service;

import com.c4.routy.domain.mypage.dto.*;
import com.c4.routy.domain.mypage.mapper.MypageQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MypageServiceTest {

    @Mock
    private MypageQueryMapper mypageQueryMapper;

    @InjectMocks
    private MypageService mypageService;

    @Test
    @DisplayName("getMyPage에서 profile이 null이면 defaultProfileImage가 세팅된다")
    void getMyPage_profileNull_usesDefaultImage() {
        // given
        Integer userNo = 1;
        int year = 2025;
        int month = 11;

        ReflectionTestUtils.setField(mypageService,
                "defaultProfileImage",
                "https://default/profile.png");

        when(mypageQueryMapper.selectProfile(userNo)).thenReturn(null);
        when(mypageQueryMapper.selectCalendarPlans(anyInt(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        when(mypageQueryMapper.selectUpcomingPlans(anyInt(), anyString()))
                .thenReturn(Collections.emptyList());
        when(mypageQueryMapper.selectTravelHistory(anyInt()))
                .thenReturn(Collections.emptyList());
        when(mypageQueryMapper.selectBookmarks(anyInt()))
                .thenReturn(Collections.emptyList());

        // when
        MyPageResponseDTO result = mypageService.getMyPage(userNo, year, month);

        // then
        assertNotNull(result);
        assertNotNull(result.getProfile());
        assertEquals("https://default/profile.png", result.getProfile().getProfileImage());
    }

    @Test
    @DisplayName("getAllTravelRecords는 mapper의 selectAllTravelRecords 결과를 그대로 반환한다")
    void getAllTravelRecords() {
        // given
        Integer userNo = 1;
        TravelRecordDTO record = new TravelRecordDTO();
        record.setPlanId(100);
        List<TravelRecordDTO> list = List.of(record);

        when(mypageQueryMapper.selectAllTravelRecords(userNo)).thenReturn(list);

        // when
        List<TravelRecordDTO> result = mypageService.getAllTravelRecords(userNo);

        // then
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getPlanId());
    }

    @Test
    @DisplayName("getAllBookmarks는 mapper의 selectAllBookmarks 결과를 그대로 반환한다")
    void getAllBookmarks() {
        // given
        Integer userNo = 1;
        BookmarkDTO bookmark = new BookmarkDTO();
        bookmark.setPlanId(200);
        List<BookmarkDTO> list = List.of(bookmark);

        when(mypageQueryMapper.selectAllBookmarks(userNo)).thenReturn(list);

        // when
        List<BookmarkDTO> result = mypageService.getAllBookmarks(userNo);

        // then
        assertEquals(1, result.size());
        assertEquals(200, result.get(0).getPlanId());
    }
}
