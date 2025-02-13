package com.junggotown.dto;

import com.junggotown.global.common.ResponseMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ExceptionResponseDto {
    private int code;
    private String message;

    @Builder
    public ExceptionResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ExceptionResponseDto> toResponseEntity(HttpStatus httpStatus) {
        return toResponseEntity(httpStatus.value(), null);
    }

    public static ResponseEntity<ExceptionResponseDto> toResponseEntity(int code, String message) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(code)
                .message(message)
                .build();

        return ResponseEntity.status(code).body(response);
    }

    public static ResponseEntity<ExceptionResponseDto> toResponseEntity(ResponseMessage responseMessage) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .code(responseMessage.getHttpStatus().value())
                .message(responseMessage.getMessage())
                .build();

        return ResponseEntity.status(responseMessage.getHttpStatus()).body(response);
    }
}
