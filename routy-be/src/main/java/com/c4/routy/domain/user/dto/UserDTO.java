package com.c4.routy.domain.user.dto;

import lombok.*;

@Data
public class UserDTO {

    private Integer userNo;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Integer age;
    private String gender;
    private String role;
    private boolean isDeleted;

}
