package com.junggotown.global.jwt;

import com.junggotown.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtProvider {
    private final Key key; // JWT secret key
    private final long accessTokenExpTime; // access 토큰 유효기간
    private final long refreshTokenExpTime; // refresh 토큰 유효기간
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>(); // 토큰 캐시

    public JwtProvider(
            @Value("${jwt.secret_key}") String secretKey,
            @Value("${jwt.expiration_time.access_token}") long accessTokenExpTime,
            @Value("${jwt.expiration_time.refresh_token}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(Member member) {
        return createToken(member, accessTokenExpTime);
    }

    /**
     * Refresh Token 생성
     * @param member
     * @return Access Token String
     */
    public String createRefreshToken(Member member) {
        return createToken(member, refreshTokenExpTime);
    }

    /**
     * JWT 생성
     * @param member
     * @param expireTime
     * @return JWT String
     */
    private String createToken(Member member, long expireTime) {
        String userId = member.getUserId();
        String existingToken = tokenCache.get(userId);

        // 기존 토큰이 유효하면 재사용
        if (existingToken != null && validateToken(existingToken)) {
            return existingToken;
        }

        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("userName", member.getUserName());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        tokenCache.put(userId, token);

        return token;
    }

    /**
     * HTTP 요청에서 Token 추출
     * @param request
     * @return token
     */
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 제거
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) { // 쿠키 이름이 "refresh_token"인 경우
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * Token에서 userId 추출
     * @param token
     * @return userId
     */
    public String getUserId(String token) {
        return parseClaims(token).get("userId", String.class);
    }


    /**
     * Request에서 userId 추출
     * @param request
     * @return userId
     */
    public String getUserId(HttpServletRequest request) {
        return parseClaims(resolveToken(request)).get("userId", String.class);
    }

    /**
     * Request에서 userName 추출
     * @param request
     * @return userName
     */
    public String getUserName(HttpServletRequest request) {
        return parseClaims(resolveToken(request)).get("userName", String.class);
    }


    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    /**
     * JWT Claims 추출
     * @param token
     * @return JWT Claims
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
