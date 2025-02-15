package com.junggotown.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConfirmDto {
    private int amount;
    private String orderId;
    private String paymentKey;

    @Builder
    public ConfirmDto(int amount, String orderId, String paymentKey) {
        this.amount = amount;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
    }

    public static ConfirmDto createConfirmDto(int amount, String orderId, String paymentKey) {
        return ConfirmDto.builder()
                .amount(amount)
                .orderId(orderId)
                .paymentKey(paymentKey)
                .build();
    }
}
