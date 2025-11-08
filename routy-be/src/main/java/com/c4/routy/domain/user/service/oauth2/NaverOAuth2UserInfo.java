package com.c4.routy.domain.user.service.oauth2;

import lombok.extern.slf4j.Slf4j;

import java.time.Year;
import java.util.Map;

// 네이버 정보 추출
@Slf4j
public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        // 네이버는 response 객체 안에 실제 데이터가 들어있음
        this.response = (Map<String, Object>) attributes.get("response");
        log.info("Naver OAuth2 사용자 정보 초기화: {}", response);
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) response.get("id");  // 네이버 고유 ID
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getName() {
        return (String) response.get("name");
    }

    @Override
    public String getPhone() {
        // 010-1234-5678
        String mobile = (String) response.get("mobile");

        if (mobile != null) {
            log.info("네이버 전화번호: {}", mobile);
        }

        return mobile;
    }

    @Override
    public String getGender() {
        String genderCode = (String) response.get("gender");

        if (genderCode == null) {
            return null;
        }

        // M -> 남성, F -> 여성
        if ("M".equals(genderCode)) {
            return "남성";
        } else if ("F".equals(genderCode)) {
            return "여성";
        }

        return null;
    }

    @Override
    public Integer getAge() {
        String birthYear = (String) response.get("birthyear");

        if (birthYear == null) {
            return null;
        }

        try {
            int currentYear = Year.now().getValue();
            int birthYearInt = Integer.parseInt(birthYear);
            int age = currentYear - birthYearInt + 1;  // 한국식 나이

            log.info("네이버 나이 계산: 출생연도={}, 나이={}", birthYear, age);

            return age;
        } catch (NumberFormatException e) {
            log.warn("출생연도 파싱 실패: {}", birthYear);
            return null;
        }
    }

    @Override
    public String getImageUrl() {
        return (String) response.get("profile_image");
    }

    // 생일(MM-DD)
    public String getBirthday() {
        return (String) response.get("birthday");
    }

    // 출생년도
    public String getBirthYear() {
        return (String) response.get("birthyear");
    }
}