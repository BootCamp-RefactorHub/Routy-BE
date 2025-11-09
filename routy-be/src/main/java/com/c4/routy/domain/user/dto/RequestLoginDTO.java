package com.c4.routy.domain.user.dto;

import lombok.Data;

@Data
public class RequestLoginDTO {
    private String email;
    private String password;
}
