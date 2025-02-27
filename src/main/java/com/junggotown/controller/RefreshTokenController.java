package com.junggotown.controller;

import com.junggotown.global.jwt.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Tag(name = "토큰", description = "토큰 api")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "토큰 재발급", description = "refresh 토큰을 전송하여 access 토큰을 재발급 받습니다.")
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return refreshTokenService.regenerateAccessToken(refreshToken);
    }

}
