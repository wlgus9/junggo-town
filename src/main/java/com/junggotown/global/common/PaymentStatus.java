package com.junggotown.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    WAIT(1, "결제대기"),
    DEPOSIT(2, "입금완료"),
    CANCEL(3, "결제취소");

    private final int code;
    private final String description;
}
