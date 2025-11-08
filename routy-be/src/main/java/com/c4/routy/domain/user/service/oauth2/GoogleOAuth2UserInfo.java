package com.c4.routy.domain.user.service.oauth2;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// Google에서 응답하는 OAuth2 사용자 정보 추출
@Slf4j
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        log.info("Google OAuth2 사용자 정보 초기화: {}", attributes);
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");  // Google 고유 ID
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPhone() {
        // Google은 전화번호를 기본 제공하지 않음
        return null;
    }

    @Override
    public String getGender() {
        // Google은 성별을 기본 제공하지 않음
        return null;
    }

    @Override
    public Integer getAge() {
        // Google은 나이를 기본 제공하지 않음
        return null;
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}