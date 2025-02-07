package com.junggotown.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ON_SALE(1, "판매중"),
    SALE_STOP(2, "판매중지"),
    SOLD_OUT(3, "판매완료");

    private final int code;
    private final String description;
}
