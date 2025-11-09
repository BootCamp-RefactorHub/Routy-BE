package com.c4.routy.domain.user.websecurity;

import com.c4.routy.domain.user.service.CustomOAuth2UserService;
import com.c4.routy.domain.user.service.OAuth2LoginSuccessHandler;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

// 시큐리티 설정 클래스
// 암호화 빈등록
// 필터 체인 등록
// AuthenticationManager 등록
@Slf4j
@Configuration
public class WebSecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider,
                             CustomOAuth2UserService customOAuth2UserService,
                             OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                // csrf 토큰 사용 X
                .csrf(csrf -> csrf.disable())

                // cors 설정 제거 (WebConfig.java에서 설정한 것으로 적용하기 위해)
                .cors(withDefaults())

                // 해당 경로 요청 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("*").permitAll()
                        .requestMatchers("/user/register", "/validation/sendmail").permitAll()
                        .requestMatchers("/api/login", "/api/signup", "/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/file/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated())

                // OAuth2.0 Client를 위한 요청 허용
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                // 세션 방식 사용 X
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 필터 추가
                .addFilter(getAuthenticationManager(authenticationManager(), jwtUtil))

                // 로그인 후 요청에 대한 필터 (JWT 토큰 인증)
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        log.info("요청: {}", http);
        return http.build();
    }

    private Filter getAuthenticationManager(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        return new AuthenticationFilter(authenticationManager, jwtUtil);
    }
}