package com.junggotown.domain;

import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID paymentId;

    private Long productId; // 상품 정보
    private String buyerId; // 구매자 정보

    @Builder
    public Orders(UUID paymentId, Long productId, String buyerId) {
        this.paymentId = paymentId;
        this.productId = productId;
        this.buyerId = buyerId;
    }

    public static Orders getOrders(UUID paymentId, Long productId, String buyerId) {
        return Orders.builder()
                .paymentId(paymentId)
                .productId(productId)
                .buyerId(buyerId)
                .build();
    }
}
