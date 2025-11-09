package com.c4.routy.domain.user.service;

import com.c4.routy.domain.user.entity.UserEntity;
import com.c4.routy.domain.user.repository.UserRepository;
import com.c4.routy.domain.user.websecurity.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // UserDetailsService에 의한 로그인을 위한 DB 조회용 메서드
    // provider에서 userService가 호출하는 메서드
    // 스프링 시큐리티 사용 시 프로바이더에서 활용할 로그인용 메서드(UserDetails 타입을 반환하는 메서드)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 쿼리 메소드를 활용한 아이디 where절을 활용
        UserEntity loginUser = userRepository.findByEmail(email);

        // 사용자가 로그인 시 아이디를 잘못 입력했다면
        if(loginUser == null) {
            throw new UsernameNotFoundException(email + "아이디의 회원은 존재하지 않습니다.");
        }

        if(loginUser.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다.");
        }

        // DB에서 조회된 해당 아이디의 회원이 가진 권한들을 가져와 List<GrandtedAuthority>로 전환
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(loginUser.getRole()));
        return new CustomUserDetails(loginUser);
    }

    /**
     * 회원 번호로 UserDetails 조회 (JWT 토큰 검증용)
     */
    public UserDetails loadUserByUserNo(Integer userNo) throws UsernameNotFoundException {

        UserEntity user = userRepository.findById(userNo)
                .orElseThrow(() -> new UsernameNotFoundException(userNo + "번 회원은 존재하지 않습니다."));

        if(user.isDeleted()) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetails(user);
    }

    /**
     * 로그아웃 처리
     * 1. HttpOnly 쿠키 삭제
     * 2. SecurityContext 초기화
     */
    @Override
    public void logout(HttpServletResponse response) {
        // 현재 인증된 사용자 정보 로깅
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("로그아웃 처리 시작 - 사용자: {}", username);
        }

        // HttpOnly 쿠키 삭제
        deleteCookie(response);

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();

        log.info("로그아웃 처리 완료 - 쿠키 삭제 및 SecurityContext 초기화");
    }

    /**
     * 현재 인증 상태 확인
     */
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 현재 인증된 사용자 이름 반환
     */
    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }

        return null;
    }



    /**
     * HttpOnly 쿠키 삭제 (private helper method)
     */
    private void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");  // 홈화면으로
        cookie.setMaxAge(0);  // 즉시 만료
        cookie.setAttribute("SameSite", "Lax");

        response.addCookie(cookie);
        log.info("HttpOnly 쿠키 삭제 완료");
    }
}
