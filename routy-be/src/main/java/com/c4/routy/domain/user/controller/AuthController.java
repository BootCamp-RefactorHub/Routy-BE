package com.c4.routy.domain.user.controller;

import com.c4.routy.domain.user.dto.RequestChangePwdDTO;
import com.c4.routy.domain.user.dto.RequestModifyUserInfoDTO;
import com.c4.routy.domain.user.dto.ResponseAuthStatusDTO;
import com.c4.routy.domain.user.dto.ResponseLogoutDTO;
import com.c4.routy.domain.user.service.AuthService;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // health check
    @GetMapping("/health")
    public String health(){
        return "Routy 서버 동작 중....";
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseLogoutDTO> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(ResponseLogoutDTO.success());
    }

    // 인증상태를 확인
    // 리펙토링 전 코드
//    @GetMapping("/status")
//    public ResponseEntity<ResponseAuthStatusDTO> checkAuthStatus() {
//
//        if (authService.isAuthenticated()) {
//            return ResponseEntity.ok(
//                    ResponseAuthStatusDTO.authenticated(authService.getCurrentUsername())
//            );
//        }
//        return ResponseEntity.ok(ResponseAuthStatusDTO.notAuthenticated());
//    }

    // 리펙토링 후 코드
    /* 리펙토링 한 이유
     * 기존의 authService.getCurrentUsername()은 내부적으로 SecurityContextHolder를 직접 참조하여 보안 정보에 의존하고 있다.
     * 이를 @AuthenticationPrincipal 주입 방식으로 변경함으로써,
     * 컨트롤러 단계에서 이미 검증된 사용자 객체를 직접 사용하게 하여 서비스 레이어와의 불필요한 결합도를 낮추고 로직의 가독성을 높였다
     * 정리하면 getCurrentUsername으로 로그인 정보를 가져오는 것이 아니라 이미 @AuthenticationPrincipal를 통해 정보를 가져왔으니
     * 거기서 정보를 추출하자 */
    @GetMapping("/status")
    public ResponseEntity<ResponseAuthStatusDTO> checkAuthStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        if(userDetails != null ){
            return  ResponseEntity.ok(
                    ResponseAuthStatusDTO.authenticated(userDetails.getUsername())
            );
        }
        return ResponseEntity.ok(ResponseAuthStatusDTO.notAuthenticated());
    }

    // 비밀번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<String> changePwd(@RequestBody RequestChangePwdDTO newPwd) {
        try {
            authService.modifyPwd(newPwd);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호 변경에 실패했습니다: " + e.getMessage());
        }
    }

    // 회원정보 변경
    @PutMapping(
            value = "/modifyuserinfo",
            consumes = {"multipart/form-data", "multipart/mixed"}
    )
    public ResponseEntity<String> modifyUserInfo(
            @RequestPart(value = "newUserInfo", required = false) RequestModifyUserInfoDTO newUserInfo,
            @RequestPart(value = "profile", required = false) MultipartFile profile,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userNo = userDetails.getUserNo();
        String message = authService.modifyUserInfo(newUserInfo, userNo, profile);
        return ResponseEntity.ok(message);
    }

    // 이메일 찾기
    @GetMapping("/find-email")
    public ResponseEntity<?> findEmail(@RequestParam String username,
                                       @RequestParam String phone) {
        return ResponseEntity.ok().body(authService.findEmail(username, phone));
    }

}