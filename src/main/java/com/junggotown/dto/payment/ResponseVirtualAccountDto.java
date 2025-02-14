package com.junggotown.dto.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // DTO에 없는 필드가 JSON에 포함되어 있을 때 무시
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드는 JSON 변환 시 제외
public class ResponseVirtualAccountDto {
    @JsonProperty("mId")
    @Comment("가맹점 ID")
    private String mId;

    @JsonProperty("paymentKey")
    @Comment("결제 고유 키")
    private String paymentKey;

    @JsonProperty("orderId")
    @Comment("주문 ID")
    private String orderId;

    @JsonProperty("orderName")
    @Comment("구매상품명")
    private String orderName;

    @JsonProperty("status")
    @Comment("결제 처리 상태")
    private String status;

    @JsonProperty("requestedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Comment("결제 요청 일시")
    private LocalDateTime requestedAt;

    @JsonProperty("approvedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @Comment("결제 승인 일시")
    private LocalDateTime approvedAt;

    @JsonProperty("virtualAccount")
    private VirtualAccount virtualAccount;

    @JsonProperty("totalAmount")
    @Comment("총 결제 금액")
    private int totalAmount;

    @Builder
    public ResponseVirtualAccountDto(String mId,String paymentKey, String orderId, String orderName, String status
            , LocalDateTime requestedAt, LocalDateTime approvedAt, VirtualAccount virtualAccount, int totalAmount
    ) {
        this.mId = mId;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.virtualAccount = virtualAccount;
        this.totalAmount = totalAmount;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VirtualAccount {
        @JsonProperty("accountNumber")
        @Comment("가상계좌번호")
        private String accountNumber;

        @JsonProperty("bankCode")
        @Comment("은행 코드")
        private String bankCode;

        @JsonProperty("customerName")
        @Comment("예금주명")
        private String customerName;

        @JsonProperty("dueDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
        @Comment("입금 만료 일시")
        private LocalDateTime dueDate;

        @JsonProperty("expired")
        @Comment("입금 만료 여부")
        private boolean expired;

        @JsonProperty("settlementStatus")
        @Comment("결제 상태")
        private String settlementStatus;

        @JsonProperty("refundStatus")
        @Comment("환불 처리 상태")
        private String refundStatus;

        @JsonProperty("refundReceiveAccount")
        @Comment("환불 수취 계좌 정보")
        private RefundReceiveAccount refundReceiveAccount;

        @Builder
        public VirtualAccount(String accountNumber, String bankCode, String customerName, LocalDateTime dueDate
                , boolean expired, String settlementStatus, String refundStatus, RefundReceiveAccount refundReceiveAccount
        ) {
            this.accountNumber = accountNumber;
            this.bankCode = bankCode;
            this.customerName = customerName;
            this.dueDate = dueDate;
            this.expired = expired;
            this.settlementStatus = settlementStatus;
            this.refundStatus = refundStatus;
            this.refundReceiveAccount = refundReceiveAccount;
        }
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefundReceiveAccount {
        @JsonProperty("bankCode")
        @Comment("은행 코드")
        private String bankCode;

        @JsonProperty("accountNumber")
        @Comment("계좌번호")
        private String accountNumber;

        @JsonProperty("holderName")
        @Comment("예금주명")
        private String holderName;

        @Builder
        public RefundReceiveAccount(String bankCode, String accountNumber, String holderName) {
            this.bankCode = bankCode;
            this.accountNumber = accountNumber;
            this.holderName = holderName;
        }
    }
}
