package com.junggotown.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseDto {
    private boolean success;
    private String message;

    @Builder
    public ApiResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
