package com.c4.routy.domain.user.websecurity;

import com.c4.routy.domain.user.dto.RequestLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

// 로그인 요청을 가로채는 필터
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    // 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLoginDTO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDTO.class);

            log.info("로그인 시도 - 이메일: {}", creds.getEmail());

            // getAuthenticationManager()가 그림 상에서 프로바이더 매니저임.
            // usernamepasswordauthenticationtoken에 이메일, 패스워드평문, 권한을 담음
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtUtil.getToken(authResult);

        // HttpOnly 쿠키로 토큰 설정
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);  // JavaScript로 접근 불가 (XSS 방지)
        cookie.setSecure(false);   // 개발 환경: false, 프로덕션(HTTPS): true
        cookie.setPath("/");       // 모든 경로에서 쿠키 전송
        cookie.setMaxAge(86400);   // 1일 (초 단위)
        cookie.setAttribute("SameSite", "Lax"); // CSRF 방어

        response.addCookie(cookie); // 응답에 쿠키 담기
        response.setStatus(HttpServletResponse.SC_OK);  // HTTP 응답 상태 코드를 200 (성공)

        log.info("로그인 성공 - 사용자: {}, 토큰 발급 완료", authResult.getName());
    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.error("로그인 실패: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\": \"" + failed.getMessage() + "\"}");
    }
}