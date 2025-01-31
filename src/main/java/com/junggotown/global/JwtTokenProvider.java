package com.junggotown.global;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenProvider {
    // JWT secret key
    private static final String SECRET_KEY = "your_secret_key";

    // 토큰 유효기간 (1시간)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // JWT Payload에 들어갈 데이터 (사용자 정보 등)
                .setIssuedAt(new Date())  // 토큰 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)  // 서명
                .compact();
    }

    // JWT 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
