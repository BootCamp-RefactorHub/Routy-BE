package com.c4.routy.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외가 발생한 경우 정해진 포멧으로 반환하기 위한 핸들러
 *
 * BusinessException이 발생한 경우 사전에 정의한 ErrorCode Enum에 있는 메시지 등을 반환해준다.
 */
@RestControllerAdvice
public class BusinessExceptionHandler {

    /**
     * 비즈니스 예외가 발생한 경우
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    /**
     * 예기치 못한 예외가 발생한 경우
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
