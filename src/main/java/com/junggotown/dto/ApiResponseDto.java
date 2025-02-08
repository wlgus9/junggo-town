package com.junggotown.dto;

import com.junggotown.global.commonEnum.ResponseMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseDto<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;

    @Builder
    public ApiResponseDto(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseDto<T> response(ResponseMessage responseMessage) {
        return response(responseMessage, null);
    }

    public static <T> ApiResponseDto<T> response(ResponseMessage responseMessage, T data) {
        return ApiResponseDto.<T>builder()
                .httpStatus(responseMessage.getHttpStatus())
                .message(responseMessage.getMessage())
                .data(data)
                .build();
    }
}
