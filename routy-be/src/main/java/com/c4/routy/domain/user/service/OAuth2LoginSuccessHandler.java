package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.websecurity.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        String token = jwtUtil.getToken(authResult);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(3600)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/");
    }
}

