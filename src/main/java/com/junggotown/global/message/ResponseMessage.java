package com.junggotown.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다.")
    , MEMBER_JOIN_SUCCESS(HttpStatus.OK, "회원가입이 완료되었습니다.")
    , BOARD_WRITE_SUCCESS(HttpStatus.OK, "글 작성이 완료되었습니다.")

    , LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.")
    , MEMBER_JOIN_DUPLICATE(HttpStatus.BAD_REQUEST, "아이디가 중복되었습니다.")
    , MEMBER_JOIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.")
    , BOARD_WRITE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "글 작성에 실패했습니다.")
    , MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")
    , INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
