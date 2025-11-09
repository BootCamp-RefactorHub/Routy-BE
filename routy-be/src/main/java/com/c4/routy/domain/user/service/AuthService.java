package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.dto.RequestChangePwdDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    void logout(HttpServletResponse response);

    boolean isAuthenticated();

    String getCurrentUsername();

    UserDetails loadUserByUserNo(Integer userNo);

    void modifyPwd(RequestChangePwdDTO newPwd);
}
