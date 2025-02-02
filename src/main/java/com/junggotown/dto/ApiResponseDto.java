package com.junggotown.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseDto {
    private boolean success;
    private String message;
    private String token;

    @Builder
    public ApiResponseDto(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }
}
