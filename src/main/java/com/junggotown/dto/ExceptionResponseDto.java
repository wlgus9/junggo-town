package com.junggotown.dto;

import com.junggotown.global.commonEnum.ResponseMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ExceptionResponseDto {
    private HttpStatus httpStatus;
    private String message;

    @Builder
    public ExceptionResponseDto(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ResponseEntity<ExceptionResponseDto> toResponseEntity(HttpStatus httpStatus, String message) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .httpStatus(httpStatus)
                .message(message)
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    public static ResponseEntity<ExceptionResponseDto> toResponseEntity(ResponseMessage responseMessage) {
        ExceptionResponseDto response = ExceptionResponseDto.builder()
                .httpStatus(responseMessage.getHttpStatus())
                .message(responseMessage.getMessage())
                .build();

        return ResponseEntity.status(responseMessage.getHttpStatus()).body(response);
    }
}
