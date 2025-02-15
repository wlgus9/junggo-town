package com.junggotown.dto.payment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WebHookDto {
    private LocalDateTime createdAt;
    private String secret;
    private String orderId;
    private String status;
    private String transactionKey;

    @Builder
    public WebHookDto(LocalDateTime createdAt, String secret, String orderId, String status, String transactionKey) {
        this.createdAt = createdAt;
        this.secret = secret;
        this.orderId = orderId;
        this.status = status;
        this.transactionKey = transactionKey;
    }
}
