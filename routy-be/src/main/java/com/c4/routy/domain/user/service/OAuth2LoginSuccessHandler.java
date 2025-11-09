package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.websecurity.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authResult)
            throws IOException {

        log.info("OAuth2 로그인 성공! - 사용자: {}", authResult.getName());

        // JWT 토큰 생성
        String token = jwtUtil.getToken(authResult);

        // 일반 로그인과 동일한 쿠키명 "token" 사용
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // 개발: false, 프로덕션: true
        cookie.setPath("/");
        cookie.setMaxAge(86400);  // 1일
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);

        log.info("OAuth2 JWT 토큰 발급 완료");

        // OAuth2 로그인 성공을 프론트엔드에 알리기 위한 쿼리 파라미터 추가
        // 프론트엔드에서 이 파라미터를 감지하고 상태를 동기화함
        String redirectUrl = "http://localhost:5173/login?oauth2Login=success";

        log.info("OAuth2 로그인 리다이렉트: {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}