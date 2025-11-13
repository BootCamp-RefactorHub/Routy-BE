package com.c4.routy.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationEmailService {
    private final JavaMailSender javaMailSender;

    // 이메일별 인증번호 저장 (이메일 -> 인증번호)
    private final Map<String, Integer> verificationCodes = new ConcurrentHashMap<>();

    public static int createNumber() {
        return (int)(Math.random() * (90000)) + 100000;
    }

    // 메일 보내기
    public int sendMail(String mail) {
        if(mail.equals("")) {
            return 0;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        String senderEmail = "indy03222100@gmail.com";
        int number = createNumber();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Routy 인증번호");
            String body = "";
            body += "<h3>" + "인증번호 입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            message.setText(body, "UTF-8", "html");
            log.info("서비스발송 번호: {}", number);

            if(body.equals("") || number == 0) {
                return 0;
            }
            javaMailSender.send(message);

            // 이메일별 인증번호 저장
            verificationCodes.put(mail, number);
            return number;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 인증 확인
    public String confirm(String email, Integer number) {

        // Map에서 해당 이메일의 인증번호 확인
        Integer savedNumber = verificationCodes.get(email);
            log.info("서비스확인 번호: {}", savedNumber);
        if (savedNumber != null && savedNumber.equals(number)) {

            // 인증 성공 후 삭제
            verificationCodes.remove(email);
            return "인증 성공! 인증이 완료되었습니다.";
        }
        return "인증 실패! 다시 인증을 시도하세요";
    }
}
