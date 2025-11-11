package com.c4.routy.domain.user.dto;

import lombok.Data;

@Data
public class RequestModifyUserInfoDTO {
    private String username;
    private String age;
    private String phone;
}
