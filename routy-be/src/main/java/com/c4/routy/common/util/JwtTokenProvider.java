package com.c4.routy.common.util;


import com.c4.routy.domain.user.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration_time}")
    private long expirationTime;

    private Key getSigningKey() {
        // Base64 인코딩된 secret을 바이트 배열로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(UserDTO userDTO){
        Claims claims = Jwts.claims().setSubject(userDTO.getEmail());
        claims.put("no", userDTO.getUserNo());
        claims.put("role",userDTO.getRole());
        claims.put("name",userDTO.getUsername());
        claims.put("email",userDTO.getEmail());
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        log.info("expirationTime: {}", expirationTime);
        log.info("now: {}", now);
        return token;
    }


    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch(Exception e){
            log.error("에러발생: {}",e.getMessage());
            return false;
        }
    }



    public String getUserEmailFromToken(String token){
        String userEmail = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();

        return userEmail;
    }

    public String getUserRoleFromToken(String token){
        String userRank = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().get("role",String.class);
        return userRank;
    }

    public String getUserNickNameFromToken(String token){
        String userNickName = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().get("nickname",String.class);
        return userNickName;
    }


    public int getUserIdFromToken(String token){
        int UserId = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody().get("id",Integer.class);
        return UserId;
    }
}
