package com.junggotown.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor
@Schema(description = "중고거래 글")
public class ProductDto {
    @NotBlank
    @Size(min = 4, max = 100, message = "상품 이름은 4~100자여야 합니다.")
    @Schema(description = "상품 이름 (최대 100자)")
    private String productName;

    @NotBlank
    @Size(min = 4, max = 500, message = "상품 상세설명은 4~500자여야 합니다.")
    @Schema(description = "상품 상세설명 (최대 500자)")
    private String productDescription;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    @Digits(integer = 10, fraction = 2, message = "가격은 최대 10자리 정수까지 가능합니다.")
    @Schema(description = "상품 가격")
    private BigDecimal price;

    @Schema(description = "사용자 아이디")
    private String userId;

    @Schema(description = "상품 아이디")
    private Long productId;

    @Builder
    public ProductDto(String productName, String productDescription, BigDecimal price, String userId, Long productId) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.userId = userId;
        this.productId = productId;
    }

    public static ProductDto getCreateDto(String productName, String productDescription, BigDecimal price) {
        return ProductDto.builder()
                .productName(productName)
                .productDescription(productDescription)
                .price(price)
                .build();
    }

    public static ProductDto getSearchDto(String userId) {
        return ProductDto.builder()
                .userId(userId)
                .build();
    }

    public static ProductDto getSearchDto(Long productId) {
        return ProductDto.builder()
                .productId(productId)
                .build();

    }
}
