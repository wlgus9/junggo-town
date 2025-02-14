package com.junggotown.domain;

import com.junggotown.dto.payment.ResponseVirtualAccountDto;
import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Comment("가맹점 ID")
    private String mId;

    @Comment("결제 고유 키")
    private String paymentKey;

    @Comment("주문 ID")
    private String orderId;

    @Comment("구매상품명")
    private String orderName;

    @Comment("결제 처리 상태")
    private String status;

    @Comment("결제 요청 일시")
    private LocalDateTime requestedAt;

    @Comment("결제 승인 일시")
    private LocalDateTime approvedAt;

    @Comment("총 결제 금액")
    private int totalAmount;

    // 가상 계좌 정보
    @Comment("가상계좌번호")
    private String accountNumber;

    @Comment("은행 코드")
    private String bankCode;

    @Comment("예금주명")
    private String customerName;

    @Comment("입금 만료 일시")
    private LocalDateTime dueDate;

    @Comment("입금 만료 여부")
    private boolean expired;

    @Comment("결제 상태")
    private String settlementStatus;

    @Comment("환불 처리 상태")
    private String refundStatus;

    // 환불 수취 계좌 정보
    @Comment("환불 은행 코드")
    private String refundBankCode;

    @Comment("환불 계좌번호")
    private String refundAccountNumber;

    @Comment("환불 예금주명")
    private String refundHolderName;

    @Builder
    public Payment(String mId, String paymentKey, String orderId, String orderName, String status,
                   LocalDateTime requestedAt, LocalDateTime approvedAt, int totalAmount,
                   String accountNumber, String bankCode, String customerName, LocalDateTime dueDate,
                   boolean expired, String settlementStatus, String refundStatus,
                   String refundBankCode, String refundAccountNumber, String refundHolderName
    ) {
        this.mId = mId;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.totalAmount = totalAmount;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
        this.customerName = customerName;
        this.dueDate = dueDate;
        this.expired = expired;
        this.settlementStatus = settlementStatus;
        this.refundStatus = refundStatus;
        this.refundBankCode = refundBankCode;
        this.refundAccountNumber = refundAccountNumber;
        this.refundHolderName = refundHolderName;
    }

    public static Payment getPaymentFromDto(ResponseVirtualAccountDto response) {
        return Payment.builder()
                .mId(response.getMId())
                .paymentKey(response.getPaymentKey())
                .orderId(response.getOrderId())
                .orderName(response.getOrderName())
                .status(response.getStatus())
                .requestedAt(response.getRequestedAt())
                .approvedAt(response.getApprovedAt())
                .totalAmount(response.getTotalAmount())
                .accountNumber(response.getVirtualAccount().getAccountNumber())
                .bankCode(response.getVirtualAccount().getBankCode())
                .customerName(response.getVirtualAccount().getCustomerName())
                .dueDate(response.getVirtualAccount().getDueDate())
                .expired(response.getVirtualAccount().isExpired())
                .settlementStatus(response.getVirtualAccount().getSettlementStatus())
                .refundStatus(response.getVirtualAccount().getRefundStatus())
                .build();
    }
}
