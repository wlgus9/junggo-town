package com.junggotown.global.jwt;

import com.junggotown.domain.Member;
import com.junggotown.global.common.ResponseMessage;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    private final long refreshTokenExpTime;
    private final JwtProvider jwtProvider;

    public RefreshTokenService(StringRedisTemplate redisTemplate,
                               @Value("${jwt.expiration_time.refresh_token}") long refreshTokenExpTime,
                               JwtProvider jwtProvider
    ) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenExpTime = refreshTokenExpTime;
        this.jwtProvider = jwtProvider;
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set("refresh:" + userId, refreshToken, refreshTokenExpTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("refresh:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }

    public ResponseEntity<String> regenerateAccessToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(401).body(ResponseMessage.MISSING_TOKEN.getMessage());
        }

        Claims claims;
        try {
            claims = jwtProvider.parseClaims(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ResponseMessage.INVALID_TOKEN.getMessage());
        }

        String userId = claims.get("userId").toString();
        String storedToken = getRefreshToken(userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return ResponseEntity.status(401).body(ResponseMessage.INVALID_TOKEN.getMessage());
        }

        // 새로운 액세스 토큰 발급
        String newAccessToken = jwtProvider.createAccessToken(Member.builder().userId(userId).build());

        return ResponseEntity.ok(newAccessToken);
    }
}
