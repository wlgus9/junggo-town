package com.junggotown.domain;

import com.junggotown.dto.product.ProductDto;
import com.junggotown.global.entity.BaseEntity;
import com.junggotown.global.message.ProductStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private int sales_status = ProductStatus.ON_SALE.getCode();

    @Builder
    public Product(Long id, String userId, String productName, String productDescription, BigDecimal price) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
    }

    public static Product getProductFromDto(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getProductId())
                .userId(productDto.getUserId())
                .productName(productDto.getProductName())
                .productDescription(productDto.getProductDescription())
                .price(productDto.getPrice())
                .build();
    }

    public static Product getProductFromDto(ProductDto productDto, String userId) {
        return Product.builder()
                .id(productDto.getProductId())
                .userId(userId)
                .productName(productDto.getProductName())
                .productDescription(productDto.getProductDescription())
                .price(productDto.getPrice())
                .build();
    }

    // 판매 상태 변경 메서드
    public void changeStatus(ProductStatus newStatus) {
        this.sales_status = newStatus.getCode();
    }
}
