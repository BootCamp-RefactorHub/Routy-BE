package com.c4.routy.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationEmailService {
    private final JavaMailSender javaMailSender;

    public static int createNumber() {
        return (int)(Math.random() * (90000)) +100000;
    }

    public int sendMail(String mail) {

        if(mail.equals("")){
            return 0;
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        String senderEmail= "indy03222100@gmail.com";
        int number = createNumber();

        try{
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO,mail);
            message.setSubject("Routy 인증번호");
            String body = "";
            body += "<h3>" + "인증번호 입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            message.setText(body,"UTF-8","html");

            if(body.equals("") || number == 0){
                return 0;
            }

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return number;
    }

}
