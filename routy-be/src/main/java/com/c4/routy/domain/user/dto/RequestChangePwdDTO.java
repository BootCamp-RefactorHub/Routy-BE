package com.c4.routy.domain.user.dto;

import lombok.Data;

@Data
public class RequestChangePwdDTO {
    private String email;
    private String newPassword;
}
