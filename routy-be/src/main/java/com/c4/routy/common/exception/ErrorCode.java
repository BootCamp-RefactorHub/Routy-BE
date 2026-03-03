package com.c4.routy.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "예기치 못한 예외가 발생하였습니다."),

    // 유저 관련 -----------------------------------------
    DUPLICATE_USER(HttpStatus.CONFLICT, "U001", "이미 존재하는 회원명입니다.");

    // API 관련 -----------------------------------------

    // -----------------------------------------

    private final HttpStatus status;
    private final String code;
    private final String message;

}
