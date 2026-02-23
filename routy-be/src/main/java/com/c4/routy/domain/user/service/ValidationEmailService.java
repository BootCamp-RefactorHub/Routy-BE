package com.c4.routy.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationEmailService {
    private final JavaMailSender javaMailSender;

    // 이메일 인증 리펙토링 전( ConcurrentHashMap 이라는 자바 내부 메모리에 임시로 인증번호 발급 저장)
    // 이럴 경우 로컬에서는 가능하지만 여러 서버에서는 사용 불가능
    // 이메일별 인증번호 저장 (이메일 -> 인증번호)
    // private final Map<String, Integer> verificationCodes = new ConcurrentHashMap<>();

    private final StringRedisTemplate redisTemplate;

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

            // 리펙토링 전
            // 이메일별 인증번호 저장
            // verificationCodes.put(mail, number);

            //리펙토링 후
            redisTemplate.opsForValue().set("AUTH_CODE:" + mail, String.valueOf(number), 3, TimeUnit.MINUTES);
            log.info("🟢 [Redis 발송 저장] 이메일: {}, 발급된 인증번호: {} (3분 유효)", mail, number);

            return number;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // 리펙토링 전
    // 인증 확인
//    public String confirm(String email, Integer number) {
//
//        // Map에서 해당 이메일의 인증번호 확인
//        Integer savedNumber = verificationCodes.get(email);
//            log.info("서비스확인 번호: {}", savedNumber);
//        if (savedNumber != null && savedNumber.equals(number)) {
//
//            // 인증 성공 후 삭제
//            verificationCodes.remove(email);
//            return "인증 성공! 인증이 완료되었습니다.";
//        }
//        return "인증 실패! 다시 인증을 시도하세요";
//    }

    //리펙토링 후
    // 인증 확인
    public String confirm(String email, Integer number) {
        // ==============================================================
        // [수정] Map 대신 Redis에서 해당 이메일의 인증번호 꺼내기
        String savedNumber = redisTemplate.opsForValue().get("AUTH_CODE:" + email);
        log.info("🔵 [Redis 검증 시도] 이메일: {}, 입력번호: {}, 저장된번호: {}", email, number, savedNumber);

        // Redis에 저장된 번호가 존재하고, 입력한 번호와 일치한다면
        if (savedNumber != null && savedNumber.equals(String.valueOf(number))) {

            // 1) 인증에 성공했으니 기존 인증번호는 삭제
            redisTemplate.delete("AUTH_CODE:" + email);

            // 2) [핵심!!] 회원가입을 위한 '합격 도장(VERIFIED)'을 10분간 저장
            redisTemplate.opsForValue().set("VERIFIED:" + email, "true", 10, TimeUnit.MINUTES);

            log.info("🟢 [Redis 인증 성공] 이메일: {} -> 'VERIFIED' 도장 10분간 저장 완료!", email);
            return "인증 성공! 인증이 완료되었습니다.";
        }
        // ==============================================================

        log.warn("🔴 [Redis 인증 실패] 이메일: {} -> 번호 불일치 또는 만료됨", email);
        return "인증 실패! 다시 인증을 시도하세요";
    }
}
