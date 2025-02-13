package com.junggotown.dto.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junggotown.domain.Orders;
import com.junggotown.domain.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null인 필드 제외
public class ResponsePaymentDto {
    private Long orderId;
    private String paymentId;
    private String paymentKey;
    private String accountNumber;
    private LocalDateTime dueDate;
    private String status;
    private String refundStatus;
    private String settlementStatus;

    @Builder
    public ResponsePaymentDto(Long orderId, String paymentId, String paymentKey, String accountNumber, LocalDateTime dueDate, String status, String refundStatus, String settlementStatus) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.paymentKey = paymentKey;
        this.accountNumber = accountNumber;
        this.dueDate = dueDate;
        this.status = status;
        this.refundStatus = refundStatus;
        this.settlementStatus = settlementStatus;
    }

    public static ResponsePaymentDto getCreateDto(Orders orders, Payment payment) {
        return ResponsePaymentDto.builder()
                .orderId(orders.getId())
                .paymentId(payment.getId().toString())
                .paymentKey(payment.getPaymentKey())
                .accountNumber(payment.getAccountNumber())
                .dueDate(payment.getDueDate())
                .status(payment.getStatus())
                .refundStatus(payment.getRefundStatus())
                .settlementStatus(payment.getSettlementStatus())
                .build();
    }
}
