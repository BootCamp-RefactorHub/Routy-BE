package com.c4.routy.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증 상태 응답 DTO
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 필드는 JSON에서 제외 (선택사항)
public class ResponseAuthStatusDTO {
    private boolean authenticated;
    private String username;

    public static ResponseAuthStatusDTO authenticated(String username) {
        return new ResponseAuthStatusDTO(true, username);
    }

    public static ResponseAuthStatusDTO notAuthenticated() {
        return new ResponseAuthStatusDTO(false, null);
    }
}