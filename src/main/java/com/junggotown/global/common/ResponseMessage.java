package com.junggotown.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다.")
    , LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공했습니다.")
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
    , VIRTUAL_ACCOUNT_CREATE_SUCCESS(HttpStatus.OK, "가상계좌 발급이 완료되었습니다.")
    , PAYMENT_DONE(HttpStatus.INTERNAL_SERVER_ERROR, "입금처리가 완료되었습니다.")
    , SEARCH_PAYMENT_SUCCESS(HttpStatus.OK, "결제내역 조회가 완료되었습니다.")

    , LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다.")
    , LOGOUT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "로그아웃에 실패했습니다.")
    , MEMBER_JOIN_DUPLICATE(HttpStatus.BAD_REQUEST, "아이디가 중복되었습니다.")
    , MEMBER_JOIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패했습니다.")
    , MEMBER_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "회원정보가 존재하지 않습니다.")
    , PRODUCT_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록에 실패했습니다.")
    , PRODUCT_SEARCH_FAIL(HttpStatus.BAD_REQUEST, "상품 조회에 실패했습니다.")
    , PRODUCT_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 정보 수정에 실패했습니다.")
    , PRODUCT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "상품 삭제에 실패했습니다.")
    , PRODUCT_IS_NOT_YOURS(HttpStatus.BAD_REQUEST, "본인이 등록한 상품이 아닙니다.")
    , PRODUCT_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "해당 상품은 존재하지 않습니다.")
    , PRODUCT_BUYER_IS_NOT_EXISTS(HttpStatus.BAD_REQUEST, "구매자가 존재하지 않습니다.")
    , CHAT_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 발신에 실패했습니다.")
    , VIRTUAL_ACCOUNT_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "가상계좌 발급에 실패했습니다.")
    , PAYMENT_WAIT_OR_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "구매자가 아직 입금 대기 중이거나 입금할 때 오류가 발생했습니다.")
    , PAYMENT_CANCEL(HttpStatus.INTERNAL_SERVER_ERROR, "구매자가 입금하기 전 결제를 취소했습니다.")
    , SEARCH_PAYMENT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "결제내역 조회에 실패했습니다.")
    , MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.")
    , INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
