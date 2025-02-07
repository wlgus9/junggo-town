package com.junggotown.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다.")
    , MEMBER_JOIN_SUCCESS(HttpStatus.OK, "회원가입이 완료되었습니다.")
    , PRODUCT_CREATE_SUCCESS(HttpStatus.OK, "상품 등록이 완료되었습니다.")
    , PRODUCT_SEARCH_SUCCESS(HttpStatus.OK, "등록한 상품이 조회되었습니다.")
    , PRODUCT_IS_EMPTY(HttpStatus.OK, "등록한 상품이 없습니다.")
    , PRODUCT_UPDATE_SUCCESS(HttpStatus.OK, "상품 정보 수정이 완료되었습니다.")
    , PRODUCT_SALESTOP_SUCCESS(HttpStatus.OK, "상품 상태가 판매중지로 변경되었습니다.")
    , PRODUCT_SOLDOUT_SUCCESS(HttpStatus.OK, "상품 상태가 판매완료로 변경되었습니다.")
    , PRODUCT_DELETE_SUCCESS(HttpStatus.OK, "상품 삭제가 완료되었습니다.")
    , CHAT_SEND_SUCCESS(HttpStatus.OK, "채팅 발신이 완료되었습니다.")
    , CHAT_SEARCH_SUCCESS(HttpStatus.OK, "채팅 조회가 완료되었습니다.")
    , CHAT_IS_EMPTY(HttpStatus.OK, "조회된 채팅이 없습니다.")

    , LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.")
    , MEMBER_JOIN_DUPLICATE(HttpStatus.BAD_REQUEST, "아이디가 중복되었습니다.")
    , MEMBER_JOIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.")
    , PRODUCT_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록에 실패했습니다.")
    , PRODUCT_SEARCH_FAIL(HttpStatus.BAD_REQUEST, "상품 조회에 실패했습니다.")
    , PRODUCT_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 정보 수정에 실패했습니다.")
    , PRODUCT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 삭제에 실패했습니다.")
    , PRODUCT_IS_NOT_YOURS(HttpStatus.BAD_REQUEST, "본인이 등록한 상품이 아닙니다.")
    , PRODUCT_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 상품은 존재하지 않습니다.")
    , PRODUCT_BUYER_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "구매자가 존재하지 않습니다.")
    , CHAT_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 발신에 실패했습니다.")
    , CHAT_SEARCH_FAIL(HttpStatus.BAD_REQUEST, "채팅 조회에 실패했습니다.")
    , MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")
    , INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
