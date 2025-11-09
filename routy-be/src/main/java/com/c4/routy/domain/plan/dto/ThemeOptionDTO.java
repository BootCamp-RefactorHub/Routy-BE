package com.c4.routy.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//수정 페이지 테마 옵션
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeOptionDTO {
    private String code;      // NATURE, CULTURE ...
    private String name;      // 자연, 인문(문화/예술/역사) ...
}
