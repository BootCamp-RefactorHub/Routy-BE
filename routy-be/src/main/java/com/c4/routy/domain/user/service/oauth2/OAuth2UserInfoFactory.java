package com.c4.routy.domain.user.service.oauth2;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// OAuth2 Provider별 UserInfo 객체 생성 팩토리 클래스 (팩토리 패턴)
// provider 별로 attributes를 다른 객체로 반환함.
@Slf4j
public class OAuth2UserInfoFactory {

    // registrationId에 따라 적절한 OAuth2UserInfo 구현체를 반환
    // OAuth2 Provider 이름 (google, naver, kakao)
    // OAuth2User의 attributes
    // OAuth2UserInfo 구현체
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        log.info("OAuth2UserInfo 생성 - Provider: {}", registrationId);

        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "naver" -> new NaverOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth2 Provider입니다: " + registrationId);
        };
    }
}