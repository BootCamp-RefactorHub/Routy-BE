package com.c4.routy.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .timestamp(java.time.LocalDateTime.now())
                        .status(errorCode.getStatus().value())  // 200, 404, 409, 500, ... 등 HTTP 상태의 코드값
                        .code(errorCode.getCode())              // 새로 예외 처리를 위해 정의한 ENUM 타입
                        .message(errorCode.getMessage())        // ENUM 타입에서 정해놓은 메시지
                        .build()
                );
    }
}
