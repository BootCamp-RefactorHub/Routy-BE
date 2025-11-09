package com.c4.routy.domain.user.controller;

import com.c4.routy.domain.user.dto.RequestRegistUserDTO;
import com.c4.routy.domain.user.dto.ResponseRegistUser;
import com.c4.routy.domain.user.dto.UserDTO;
import com.c4.routy.domain.user.service.SignUpService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class SignUpController {

    private final ModelMapper modelMapper;
    private final SignUpService signUpService;

    @Autowired
    public SignUpController(ModelMapper modelMapper, SignUpService signUpService) {
        this.modelMapper = modelMapper;
        this.signUpService = signUpService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseRegistUser> signUp(@RequestBody RequestRegistUserDTO newUser) {

        UserDTO userDTO = modelMapper.map(newUser, UserDTO.class);
        signUpService.registUser(userDTO);
        ResponseRegistUser response = new ResponseRegistUser("회원가입이 완료되었습니다.");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
