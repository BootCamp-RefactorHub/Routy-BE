package com.c4.routy.domain.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int userNo;

    private String username;

    private String email;

    private String password;

    private int age;

    private String gender;

    private String role;

    private int isDeleted;

}
