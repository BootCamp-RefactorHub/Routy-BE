package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.dto.RequestChangePwdDTO;
import com.c4.routy.domain.user.dto.RequestModifyUserInfoDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService extends UserDetailsService {

    void logout(HttpServletResponse response);

    boolean isAuthenticated();

    String getCurrentUsername();

    UserDetails loadUserByUserNo(Integer userNo);

    void modifyPwd(RequestChangePwdDTO newPwd);

    String modifyUserInfo(RequestModifyUserInfoDTO newUserInfo, Integer userNo, MultipartFile profile);
}
