package com.junggotown.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    DONE("DONE", "입금완료")
    , CANCELED("CANCELED", "결제취소")
    , WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT", "입금대기 또는 입금오류");

    private final String status;
    private final String description;
}
