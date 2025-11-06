package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.dto.ResponseRegistUser;
import com.c4.routy.domain.user.dto.UserDTO;

public interface SignUpService {
    void registUser(UserDTO userDTO);
}
