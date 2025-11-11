package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// 일정 수정 화면의 테마 옵션 DTO
// PlanEditResponseDTO.themeOptions 에서 사용
// 화면에서 체크박스로 보여줄 전체 테마 목록
//수정 페이지 테마 옵션
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeOptionDTO {
    private String code;      // 테마 코드 (NATURE, CULTURE ...)
    private String name;      // 사용자에게 보여줄 이름 (자연, 인문(문화/예술/역사) ...)
}
