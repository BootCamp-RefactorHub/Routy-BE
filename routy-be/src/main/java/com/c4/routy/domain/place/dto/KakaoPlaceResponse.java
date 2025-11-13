package com.c4.routy.domain.place.dto;

import lombok.Data;
import java.util.List;

@Data
public class KakaoPlaceResponse {
    private Meta meta;
    private List<KakaoPlaceDocument> documents;

    @Data
    public static class Meta {
        private Integer total_count;        // 검색된 총 문서 수
        private Integer pageable_count;     // 페이지 가능한 문서 수
        private Boolean is_end;             // 현재 페이지가 마지막인지
    }
}