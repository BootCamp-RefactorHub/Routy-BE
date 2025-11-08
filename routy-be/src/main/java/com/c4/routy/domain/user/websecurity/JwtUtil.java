package com.c4.routy.domain.user.websecurity;

import com.c4.routy.domain.user.service.AuthService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final String expirationTime;
    private final AuthService authService;

    public JwtUtil(@Value("${token.secret}") String key,
                   @Value("${token.expiration_time}") String expirationTime,
                   AuthService authService) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
        this.authService = authService;
    }

    // JWT 토큰 생성 (일반 로그인 + OAuth2 로그인 모두 지원)
    public String getToken(Authentication authResult) {
        log.info("JWT 토큰 생성 시작 - Principal 타입: {}", authResult.getPrincipal().getClass().getSimpleName());

        String email;
        Integer userNo;
        List<String> roles;

        // Principal 타입에 따라 분기처리하는 로직
        if (authResult.getPrincipal() instanceof CustomUserDetails) {

            // 일반 로그인
            CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
            email = userDetails.getUsername();
            userNo = userDetails.getUserNo();
            log.info("일반 로그인 - 이메일: {}, 회원번호: {}", email, userNo);

        } else if (authResult.getPrincipal() instanceof OAuth2User) {

            // OAuth2 로그인
            OAuth2User oAuth2User = (OAuth2User) authResult.getPrincipal();
            email = oAuth2User.getAttribute("email");

            // DB에서 userNo 조회
            UserDetails userDetails = authService.loadUserByUsername(email);
            if (userDetails instanceof CustomUserDetails) {
                userNo = ((CustomUserDetails) userDetails).getUserNo();
            } else {
                throw new RuntimeException("CustomUserDetails를 찾을 수 없습니다.");
            }

            log.info("OAuth2 로그인 - 이메일: {}, 회원번호: {}", email, userNo);

        } else {
            throw new IllegalArgumentException("지원하지 않는 Principal 타입: " + authResult.getPrincipal().getClass());
        }

        // 권한 추출
        roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("JWT 생성 정보 - 이메일: {}, 회원번호: {}, 권한: {}", email, userNo, roles);

        // JWT 생성
        Claims claims = Jwts.claims().setSubject(String.valueOf(userNo));
        claims.put("auth", roles);
        claims.put("email", email);

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new java.util.Date(System.currentTimeMillis()
                        + Long.parseLong(expirationTime)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return token;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("토큰의 클레임이 비어있음");
        }
        return false;
    }

    // 토큰에서 인증 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // subject에 userNo가 들어있음
        Integer userNo = Integer.parseInt(claims.getSubject());
        UserDetails userDetails = authService.loadUserByUserNo(userNo);

        // 토큰에 들어있는 권한들 추출
        Collection<GrantedAuthority> authorities;
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한이 들어있지 않은 토큰입니다.");
        } else {
            authorities = Arrays.stream(claims.get("auth").toString()
                            .replace("[", "")
                            .replace("]", "")
                            .split(", "))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    // 토큰에서 클레임 추출
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}