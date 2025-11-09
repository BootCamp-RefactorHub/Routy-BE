package com.c4.routy.domain.user.service.oauth2;

import lombok.extern.slf4j.Slf4j;

import java.time.Year;
import java.util.Map;

// 카카오 정보 추출
@Slf4j
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;

        // 카카오는 kakao_account 객체 안에 데이터가 들어있음
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        // profile은 kakao_account 안에 또 들어있음
        this.profile = (Map<String, Object>) kakaoAccount.get("profile");

        log.info("Kakao OAuth2 사용자 정보 초기화");
        log.info("- kakao_account: {}", kakaoAccount);
        log.info("- profile: {}", profile);
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        Object id = attributes.get("id");
        return id != null ? String.valueOf(id) : null;  // 카카오 고유 ID (숫자)
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }

    @Override
    public String getPhone() {
        String phoneNumber = (String) kakaoAccount.get("phone_number");
        if (phoneNumber == null) {
            return null;
        }

        // 카카오 형식: +82 10-1234-5678
        // 변환: 010-1234-5678
        String phone = phoneNumber.replace("+82 ", "0").replace(" ", "");
        log.info("카카오 전화번호 변환: {} -> {}", phoneNumber, phone);

        return phone;
    }

    @Override
    public String getGender() {
        String genderCode = (String) kakaoAccount.get("gender");

        if (genderCode == null) {
            return null;
        }

        // male -> 남성, female -> 여성
        if ("male".equals(genderCode)) {
            return "남성";
        } else if ("female".equals(genderCode)) {
            return "여성";
        }

        return null;
    }

    @Override
    public Integer getAge() {
        String birthYear = getBirthYear();

        if (birthYear == null) {
            return null;
        }

        try {
            int currentYear = Year.now().getValue();
            int birthYearInt = Integer.parseInt(birthYear);
            int age = currentYear - birthYearInt + 1;

            log.info("카카오 나이 계산: 출생연도={}, 나이={}", birthYear, age);

            return age;
        } catch (NumberFormatException e) {
            log.warn("출생연도 파싱 실패: {}", birthYear);
            return null;
        }
    }

    @Override
    public String getImageUrl() {
        return (String) profile.get("profile_image_url");
    }


    // 연령대
    public String getAgeRange() {
        return (String) kakaoAccount.get("age_range");
    }

    // 생일 (MMDD)
    public String getBirthday() {
        return (String) kakaoAccount.get("birthday");
    }

    // 출생연도
    public String getBirthYear() {
        return (String) kakaoAccount.get("birthyear");
    }
}