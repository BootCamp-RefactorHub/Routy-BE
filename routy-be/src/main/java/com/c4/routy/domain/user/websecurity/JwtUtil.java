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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// Jwt 토큰 생성 및 유효성 검사 로직있는 JwtUtil 객체
@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final String expirationTime;
    private final AuthService authService;

    public JwtUtil(@Value("${token.secret}") String key,
                   @Value("${token.expiration_time}") String expirationTime,
                   AuthService authService) {
        // String -> Key로 변환(Token Parsing할 때 사용하기 용이하도록)
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationTime = expirationTime;
        this.authService = authService;
    }

    // 일반 로그인 용 Jwt 토큰 생성기
    public String getToken(Authentication authResult) {
        log.info("로그인 성공 이후 스프링 시큐리티가 Authentication 객체로 관리되며 넘어옴: {}", authResult);

        // JWT 제작
        // 토큰의 페이로드에 아이디, 권한, 만료시간을 담기
        // 프로바이더에서 반환한 내용 중 User의 내용은 Principal로 저장되어 있음

        // 1. JWT 제작을 위한 재료 추출 (jwt.io / json web tokens)
        // 페이로드에 담을 내용 중 아이디를 추출
        String email = ((CustomUserDetails) authResult.getPrincipal()).getUsername();

        // 페이로드에 담을 내용 중 권한을 추출
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 페이로드에 담을 내용 중 userNo를 추출
        String userNo = String.valueOf(((CustomUserDetails) authResult.getPrincipal()).getUserNo());

        // 2. 재료를 활용한 JWT 제작
        log.info("회원의 이메일: {}", email);
        log.info("List<String> 형태로 뽑아낸 로그인한 회원의 권한들: {}", roles);
        log.info("회원 번호: {}", userNo);
        log.info("만료시간: {}", expirationTime);

        Claims claims = Jwts.claims().setSubject(userNo); // 등록된 클레임
        claims.put("auth", roles);                        // 비공개 클레임
        claims.put("email", email);

        String token = Jwts.builder()
                .setClaims(claims)// 등록된 클레임 + 비공개 클레임
                .setExpiration(new java.util.Date(System.currentTimeMillis()
                        + Long.parseLong(expirationTime)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return token;
    }

    // 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT Token(아무런 값이 없음");
        } catch (ExpiredJwtException e) {
            log.info("만료기간이 지남");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT Token(지원하지 않는 양식)");
        } catch (IllegalArgumentException e) {
            log.info("토큰의 클레임이 비어있음");
        }
        return false;
    }

    // 유효성 검증이 끝난 토큰에서 인증 객체(Authenticatioin)를 반환
    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        // 토큰에 들어있던 아이디로 DB에서 회원 조회하고 UserDetails로 가져옴
        // subject에 userNo가 들어있음
        Integer userNo = Integer.parseInt(claims.getSubject());
        UserDetails userDetails = authService.loadUserByUserNo(userNo);

        // 토큰에 들어있는 권한들을 List<GrantedAuthority>로 꺼내보기
        // 1. 토큰에 권한이 들어있을 때
        Collection<GrantedAuthority> authorities = null;
        if (claims.get("auth") == null) {
            throw new RuntimeException(("권한이 들어있지 않는 토큰입니다."));
        } else {
            authorities =
                    Arrays.stream(claims.get("auth").toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(", "))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        // 2. 서비스 계층의 loadUserByUsername()메서드 반환값(UserDetails)를 활용할 때
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 문자열 토큰에서 페이로드에 담긴 클레임들만 추출
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
