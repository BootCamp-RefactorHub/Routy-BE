package com.c4.routy.domain.user.websecurity;

import com.c4.routy.domain.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// 자체 서비스 로그인 토큰 발급기
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;  // 평문과 다이제스트를 비교하기 위한 도구

    @Autowired
    public JwtAuthenticationProvider(AuthService authService,
                                     PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 인증
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails userDetails = (CustomUserDetails) authService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 에외가 발생하지 않고 이 부분 이후가 실행되면 UserDetails에 담긴 인증된 회원정보로 Token을 만든다.
        // AuthenticationFilter의 successfulAuthentication의 매개변수로 들어감.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 이러 이러한 토큰을 처리할 수 있습니다.
    // 내부적으로 필터에 있는 token객체가 authentication으로 자동으로 넘어옴.
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
