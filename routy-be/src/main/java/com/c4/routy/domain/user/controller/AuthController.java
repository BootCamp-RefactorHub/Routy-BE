package com.c4.routy.domain.user.controller;

import com.c4.routy.domain.user.dto.ResponseAuthStatusDTO;
import com.c4.routy.domain.user.dto.ResponseLogoutDTO;
import com.c4.routy.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그아웃 API
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<ResponseLogoutDTO> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(ResponseLogoutDTO.success());
    }

    /**
     * 인증 상태 확인 API
     */
    @GetMapping("/auth/status")
    public ResponseEntity<ResponseAuthStatusDTO> checkAuthStatus() {
        if (authService.isAuthenticated()) {
            return ResponseEntity.ok(
                    ResponseAuthStatusDTO.authenticated(authService.getCurrentUsername())
            );
        }
        return ResponseEntity.ok(ResponseAuthStatusDTO.notAuthenticated());
    }
}