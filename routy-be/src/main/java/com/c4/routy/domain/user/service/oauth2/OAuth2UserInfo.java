package com.c4.routy.domain.user.service.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

// OAuth2 Provider별 사용자 정보 추출 인터페이스
public interface OAuth2UserInfo {

    // Provider 이름 반환 (google, naver, kakao)
    String getProvider();

    // 이메일 추출
    String getEmail();

    // 이름 추출
    String getName();

    // 전화번호 추출
    String getPhone();

    // 성별 추출
    String getGender();

    // 나이 추출
    Integer getAge();

    // 프로필 이미지 URL 추출
    String getImageUrl();

    // Provider 고유 ID 추출
    String getProviderId();
}