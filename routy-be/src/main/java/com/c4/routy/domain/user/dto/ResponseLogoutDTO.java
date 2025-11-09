package com.c4.routy.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그아웃 응답 DTO
 */
@Getter
@AllArgsConstructor
public class ResponseLogoutDTO {
    private String message;

    public static ResponseLogoutDTO success() {
        return new ResponseLogoutDTO("로그아웃 성공");
    }
}