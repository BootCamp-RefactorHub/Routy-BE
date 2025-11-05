package com.c4.routy.domain.user.security;

import com.c4.routy.domain.user.dto.UserDTO;
import com.c4.routy.domain.user.service.UserService;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        System.out.println("oAuth2User = " + oAuth2User);

        UserDTO userDTO = new UserDTO();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = null;
        String username = null;
        String name = null;


        if (attributes.containsKey("response")) {
            Map<String, Object> res = (Map<String, Object>) attributes.get("response");
            email = (String) res.get("email");
            username = (String) res.get("nickname");
            System.out.println("네이버 로그인: " + email + ", name = " + name);
        }else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");

            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                username = (String) profile.get("nickname");
            }

            System.out.println("카카오 로그인: " + email + ", name = " + name);
        }else {
            email = (String) attributes.get("email");
            username = (String) attributes.get("given_name"); // ✅ 이름만 (예: John)

            System.out.println("구글 로그인: " + email + ", name = " + name);
        }

//        if (attributes.containsKey("response")) {
//            // 네이버 응답 구조
//            Map<String, Object> res = (Map<String, Object>) attributes.get("response");
//            email = (String) res.get("email");
//            System.out.println("email = " + email);
//        }else if (attributes.containsKey("kakao_account")) {
//            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//            email = (String) kakaoAccount.get("email");
//
//            if (kakaoAccount.containsKey("profile")) {
//                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//                nickname = (String) profile.get("nickname");
//            }
//
//            System.out.println("카카오 로그인: " + email);
//        }else {
//            // 구글 응답 구조
//            email = (String) attributes.get("email");
//            System.out.println("email = " + email);
//        }



        userDTO.setEmail(email);
        userDTO.setUsername(username);
        userDTO.setRole("ROLE_일반회원");
        userDTO.setIsDeleted(0);

        UserDTO existUser = userService.findUserByEmail(userDTO);
        if (existUser == null) {
            userService.insertUser(userDTO);
        }



        String token = jwtTokenProvider.createToken(userDTO);

        String redirectUrl = "http://localhost:5173/login/success?token=" + token;

        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
