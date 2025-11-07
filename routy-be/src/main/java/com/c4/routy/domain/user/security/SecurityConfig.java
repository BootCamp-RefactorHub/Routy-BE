package com.c4.routy.domain.user.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2SuccessHandler successHandler;
    private final CustomOAuth2FailureHandler failureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    // BCrypt 단방향 암호화를 위해 bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                         //.requestMatchers("/api/auth/**", "/oauth2/**", "/login/**", "/validation/**", "/user/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("*").permitAll()
                        .anyRequest().authenticated()
                );

        httpSecurity.oauth2Login(oauth -> oauth
                .successHandler(successHandler)
                .failureHandler(failureHandler)
        );

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
