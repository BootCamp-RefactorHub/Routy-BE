package com.c4.routy.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_user")
@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Integer userNo;

    @Column(name = "username")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "image_url")
    private String imageUrl;

}
