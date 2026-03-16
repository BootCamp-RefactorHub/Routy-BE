package com.c4.routy.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "예기치 못한 예외가 발생하였습니다."),

    // 유저 관련 -----------------------------------------
    DUPLICATE_USER(HttpStatus.CONFLICT, "U001", "이미 존재하는 회원명입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "해당 유저를 찾을 수 없습니다."),
    OAuth2_NOT_PROVIDE(HttpStatus.BAD_REQUEST, "U003", "지원하지 않는 OAuth2 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "비밀번호는 8자 이상이어야 합니다."),
    MAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "U005", "메일 전송에 실패하였습니다."), // U001 중복 수정
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "U006", "인증되지 않은 사용자입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "U007", "유효하지 않은 요청입니다."),

    // 지역 관련 -----------------------------------------
    REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "해당 지역을 찾을 수 없습니다."),

    // API 관련 -----------------------------------------
    EXTERNAL_API_FAIL(HttpStatus.BAD_GATEWAY, "A001", "외부 API 호출에 실패했습니다."),

    // 일정(Plan) 관련 -----------------------------------------
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 일정을 찾을 수 없습니다."),

    // 좋아요/게시글 관련 -----------------------------------------
    CANNOT_LIKE_OWN_POST(HttpStatus.BAD_REQUEST, "L001", "본인의 글에는 좋아요를 누를 수 없습니다."),

    // 파일 관련 -----------------------------------------
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "F001", "잘못된 형식의 파일입니다."),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "F002", "파일 업로드에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
