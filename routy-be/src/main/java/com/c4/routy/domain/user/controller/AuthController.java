package com.c4.routy.domain.user.controller;

import com.c4.routy.domain.user.dto.RequestChangePwdDTO;
import com.c4.routy.domain.user.dto.RequestModifyUserInfoDTO;
import com.c4.routy.domain.user.dto.ResponseAuthStatusDTO;
import com.c4.routy.domain.user.dto.ResponseLogoutDTO;
import com.c4.routy.domain.user.service.AuthService;
import com.c4.routy.domain.user.service.AwsS3Service;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AwsS3Service awsS3Service;

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

    @PutMapping("/auth/change-password")
    public ResponseEntity<String> changePwd(@RequestBody RequestChangePwdDTO newPwd) {
        try {
            log.info("비밀번호 변경 요청 - 이메일: {}", newPwd.getEmail());
            authService.modifyPwd(newPwd);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            log.error("비밀번호 변경 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호 변경에 실패했습니다: " + e.getMessage());
        }
    }

    @PutMapping(
            value = "/auth/modifyuserinfo",
            consumes = {"multipart/form-data", "multipart/mixed"}
    )
    public ResponseEntity<String> modifyUserInfo(
            @RequestPart(value = "newUserInfo", required = false) RequestModifyUserInfoDTO newUserInfo,
            @RequestPart(value = "profile", required = false) MultipartFile profile,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        log.info("회원정보 요청: {}", newUserInfo);
        log.info("회원정보 요청: {}", profile);
        log.info("회원정보 요청: {}", userDetails.getEmail());
        Integer userNo = userDetails.getUserNo();
        String message = authService.modifyUserInfo(newUserInfo, userNo, profile);
        return ResponseEntity.ok(message);
    }

}