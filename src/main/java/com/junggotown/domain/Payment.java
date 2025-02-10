package com.junggotown.domain;

import com.junggotown.global.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 상품 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer; // 구매자 정보

    @Column(name = "payment_key", unique = true)
    private String paymentKey;

    private String virtualAccount;
    private String bankName;
    private int amount;
    private int status = PaymentStatus.WAIT.getCode();

    @Builder
    public Payment(Product product, Member buyer, String paymentKey, String virtualAccount, String bankName, int amount) {
        this.orderId = generateOrderId();
        this.product = product;
        this.buyer = buyer;
        this.paymentKey = paymentKey;
        this.virtualAccount = virtualAccount;
        this.bankName = bankName;
        this.amount = amount;
    }

    private String generateOrderId() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORDER-" + date + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
