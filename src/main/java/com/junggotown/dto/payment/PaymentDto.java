package com.junggotown.dto.payment;

import com.junggotown.domain.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private Long productId;
    private Long buyerId;
    private String paymentKey;
    private String virtualAccount;
    private String bankName;
    private int amount;
    private int status;

    @Builder
    public PaymentDto(Long id, Long productId, Long buyerId, String paymentKey, String virtualAccount, String bankName, int amount, int status) {
        this.id = id;
        this.productId = productId;
        this.buyerId = buyerId;
        this.paymentKey = paymentKey;
        this.virtualAccount = virtualAccount;
        this.bankName = bankName;
        this.amount = amount;
        this.status = status;
    }

    public static PaymentDto fromEntity(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .productId(payment.getProduct().getId())
                .buyerId(payment.getBuyer().getId())
                .paymentKey(payment.getPaymentKey())
                .virtualAccount(payment.getVirtualAccount())
                .bankName(payment.getBankName())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }
}
