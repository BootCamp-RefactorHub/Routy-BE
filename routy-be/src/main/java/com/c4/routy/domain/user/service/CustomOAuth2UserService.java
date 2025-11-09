package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.repository.UserRepository;
import com.c4.routy.domain.user.service.oauth2.OAuth2UserInfo;
import com.c4.routy.domain.user.service.oauth2.OAuth2UserInfoFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// 소셜로그인별 정보 추출
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2User 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        log.info("========== OAuth2 로그인 시작 ==========");
        log.info("Provider: {}", registrationId);

        // 팩토리 패턴으로 Provider별 정보 추출기 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                oAuth2User.getAttributes()
        );

        // 사용자 정보 추출
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String phone = oAuth2UserInfo.getPhone();
        String gender = oAuth2UserInfo.getGender();
        Integer age = oAuth2UserInfo.getAge();
        String imageUrl = oAuth2UserInfo.getImageUrl();
        String providerId = oAuth2UserInfo.getProviderId();

        // 이메일 검증
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("이메일 정보는 필수입니다.");
        }

        log.info("추출된 사용자 정보:");
        log.info("- Provider ID: {}", providerId);
        log.info("- 이메일: {}", email);
        log.info("- 이름: {}", name);
        log.info("- 전화번호: {}", phone);
        log.info("- 성별: {}", gender);
        log.info("- 나이: {}", age);
        log.info("- 이미지 URL: {}", imageUrl);

        // DB에서 사용자 조회 또는 생성
        UserEntity user = findOrCreateUser(oAuth2UserInfo);

        // Spring Security용 OAuth2User 객체 생성 및 반환
        return createOAuth2User(user, oAuth2User.getAttributes());
    }

    // 사용자 조회 또는 신규 생성
    private UserEntity findOrCreateUser(OAuth2UserInfo oAuth2UserInfo) {
        String email = oAuth2UserInfo.getEmail();
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            // 신규 사용자 생성
            log.info("신규 OAuth2 사용자 생성: {}", email);
            user = createNewUser(oAuth2UserInfo);
            userRepository.save(user);
            log.info("사용자 생성 완료 - UserNo: {}", user.getUserNo());

        } else {
            // 기존 사용자 정보 업데이트
            log.info("기존 OAuth2 사용자 로그인: {}", email);
            boolean updated = updateExistingUser(user, oAuth2UserInfo);

            if (updated) {
                userRepository.save(user);
                log.info("사용자 정보 업데이트 완료");
            }
        }

        return user;
    }

    // 신규 사용자 생성
    private UserEntity createNewUser(OAuth2UserInfo oAuth2UserInfo) {
        UserEntity user = new UserEntity();
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUsername(oAuth2UserInfo.getName());
        user.setPhone(oAuth2UserInfo.getPhone());
        user.setGender(oAuth2UserInfo.getGender());
        user.setAge(oAuth2UserInfo.getAge());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRole("ROLE_USER");
        user.setDeleted(false);
        log.info("신규사용자 생성 시 전화번호: {}", oAuth2UserInfo.getPhone());
        return user;
    }

    // 기존 사용자 정보 업데이트
    private boolean updateExistingUser(UserEntity user, OAuth2UserInfo oAuth2UserInfo) {
        boolean updated = false;

        // 이름 업데이트
        String newName = oAuth2UserInfo.getName();
        if (newName != null && !newName.equals(user.getUsername())) {
            user.setUsername(newName);
            updated = true;
        }

        // 전화번호 업데이트 (기존에 없었던 경우에만)
        String newPhone = oAuth2UserInfo.getPhone();
        if (newPhone != null && user.getPhone() == null) {
            user.setPhone(newPhone);
            updated = true;
        }

        // 성별 업데이트 (기존에 없었던 경우에만)
        String newGender = oAuth2UserInfo.getGender();
        if (newGender != null && user.getGender() == null) {
            user.setGender(newGender);
            updated = true;
        }

        // 나이 업데이트 (기존에 없었던 경우에만)
        Integer newAge = oAuth2UserInfo.getAge();
        if (newAge != null && user.getAge() == null) {
            user.setAge(newAge);
            updated = true;
        }

        // 프로필 이미지 업데이트 (항상)
        String newImageUrl = oAuth2UserInfo.getImageUrl();
        if (newImageUrl != null && !newImageUrl.equals(user.getImageUrl())) {
            user.setImageUrl(newImageUrl);
            updated = true;
        }

        return updated;
    }

    // SpringSecurity용 oauth2user객체
    private OAuth2User createOAuth2User(UserEntity user, Map<String, Object> originalAttributes) {
        // attributes에 email을 최상위 키로 추가 (DefaultOAuth2User 생성을 위해 필수)
        Map<String, Object> modifiedAttributes = new HashMap<>(originalAttributes);
        modifiedAttributes.put("email", user.getEmail());
        modifiedAttributes.put("name", user.getUsername());
        modifiedAttributes.put("userNo", user.getUserNo());

        log.info("OAuth2User 생성 완료 - email: {}, userNo: {}", user.getEmail(), user.getUserNo());
        log.info("========== OAuth2 로그인 완료 ==========");

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                modifiedAttributes,
                "email"
        );
    }
}