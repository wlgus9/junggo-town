package com.junggotown.global.exception;

import com.junggotown.dto.ExceptionResponseDto;
import com.junggotown.global.exception.board.BoardException;
import com.junggotown.global.exception.member.MemberException;
import com.junggotown.global.exception.token.InvalidTokenException;
import com.junggotown.global.exception.token.MissingTokenException;
import com.junggotown.global.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> exception(Exception e) {
        return ExceptionResponseDto.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> illegalArgumentException(IllegalArgumentException e) {
        return ExceptionResponseDto.toResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<ExceptionResponseDto> missingTokenException() {
        return ExceptionResponseDto.toResponseEntity(ResponseMessage.MISSING_TOKEN);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponseDto> invalidTokenException() {
        return ExceptionResponseDto.toResponseEntity(ResponseMessage.INVALID_TOKEN);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ExceptionResponseDto> memberException(HttpServletRequest request) {
        if(request.getRequestURI().contains("join")) {
            return ExceptionResponseDto.toResponseEntity(ResponseMessage.MEMBER_JOIN_FAIL);
        } else {
            return ExceptionResponseDto.toResponseEntity(ResponseMessage.LOGIN_FAIL);
        }
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ExceptionResponseDto> boardException(HttpServletRequest request) {
        if(request.getRequestURI().contains("write")) {
            return ExceptionResponseDto.toResponseEntity(ResponseMessage.BOARD_WRITE_FAIL);
        } else {
            return ExceptionResponseDto.toResponseEntity(ResponseMessage.BOARD_WRITE_FAIL);
        }
    }
}
