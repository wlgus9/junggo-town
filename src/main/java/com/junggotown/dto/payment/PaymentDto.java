package com.junggotown.dto.payment;

import com.junggotown.domain.Member;
import com.junggotown.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class PaymentDto {
    private BigDecimal amount;
    private String orderId = generateOrderId();
    private String orderName;
    private String customerName;
    private String bank = "20";
    private int validHours = 1;

    @Builder
    public PaymentDto(BigDecimal amount, String orderName, String customerName) {
        this.amount = amount;
        this.orderName = orderName;
        this.customerName = customerName;
    }

    public static PaymentDto createPaymentDto(Product product, Member member) {
        return PaymentDto.builder()
                .amount(product.getPrice())
                .orderName(product.getProductName())
                .customerName(member.getUserName())
                .build();
    }

    private String generateOrderId() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORDER-" + date + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}
