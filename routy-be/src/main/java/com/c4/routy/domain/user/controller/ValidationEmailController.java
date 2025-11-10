package com.c4.routy.domain.user.controller;


;
import com.c4.routy.domain.user.service.ValidationEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ValidationEmailController {

    private final ValidationEmailService emailService;

    public ValidationEmailController(ValidationEmailService validationEmailService) {
        this.emailService = validationEmailService;
    }

    // 1. 인증번호 발송
    @PostMapping("/validation/sendmail")
    public ResponseEntity<Integer> sendMail(String mail) {
        int result = emailService.sendMail(mail);
        return ResponseEntity.ok(result);  // 0 or 인증번호
    }

    // 2. 인증번호 확인
    @PostMapping("/auth/confirm")
    public ResponseEntity<String> confirm(String email, Integer number) {
        String result = emailService.confirm(email, number);
        return ResponseEntity.ok(result);  // "인증 성공" or "인증 실패"
    }
}
