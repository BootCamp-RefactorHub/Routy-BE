package com.c4.routy.domain.user.controller;


import com.c4.routy.domain.user.dto.UserDTO;
import com.c4.routy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

//    private final UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> login(@RequestBody UserDTO userDTO){
//        System.out.println("userDTO = " + userDTO);
//
//        UserDTO existUser = userService.findUserByEmail(userDTO);
//        if(existUser == null){
//            userService.insertUser(userDTO);
//            return ResponseEntity.ok("회원가입을 하였습니다.");
//        }else{
//            return ResponseEntity.ok("존재하는 회원입니다.");
//        }
//    }

}
