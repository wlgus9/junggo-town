package com.junggotown.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junggotown.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null인 필드 제외
public class ResponseProductDto {
    private Long id;
    private String userId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ResponseProductDto(Long id, String userId, String productName, String productDescription, BigDecimal price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ResponseProductDto getCreateDto(Long id) {
        return ResponseProductDto.builder()
                .id(id)
                .build();
    }

    public static ResponseProductDto getSearchDto(Product product) {
        return ResponseProductDto.builder()
                .id(product.getId())
                .userId(product.getUserId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }


}
