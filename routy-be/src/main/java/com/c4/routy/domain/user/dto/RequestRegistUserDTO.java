package com.c4.routy.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestRegistUserDTO {
    private String username;
    private String email;
    private String password;
    private String phone;
    private Integer age;
    private String gender;
}
