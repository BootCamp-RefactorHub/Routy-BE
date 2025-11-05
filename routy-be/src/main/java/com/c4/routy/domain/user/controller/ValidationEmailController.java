package com.c4.routy.domain.user.controller;


;
import com.c4.routy.domain.user.service.ValidationEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation")
@Slf4j
public class ValidationEmailController {

    private final ValidationEmailService emailService;

    public ValidationEmailController(ValidationEmailService validationEmailService) {
        this.emailService = validationEmailService;
    }

    @PostMapping("/sendmail")
    public ResponseEntity<Integer> sendMail(String mail) {
        int result = emailService.sendMail(mail);
        if(result == 0){
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(result);
    }

//    @PostMapping("/sendmailpassword")
//    public ResponseEntity<Integer> sendMailPassword(String mail, String id) {
//        int result = emailService.sendMailPassword(mail, id);
//        if(result == 0){
//            return ResponseEntity.ok(0);
//        }
//        return ResponseEntity.ok(result);
//    }
}
