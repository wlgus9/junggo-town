package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.dto.product.ResponseProductDto;
import com.junggotown.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "중고거래 상품", description = "중고거래 상품 api")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "상품 정보를 입력하여 상품을 등록합니다.")
    @PostMapping("/create")
    public ApiResponseDto<ResponseProductDto> create(@RequestBody @Valid ProductDto productDto, HttpServletRequest request) {
        return productService.create(productDto, request);
    }

    @Operation(summary = "상품 상세조회", description = "상품 아이디를 입력하여 상품을 상세조회합니다.")
    @GetMapping(value = "/search", params = "productId")
    public ApiResponseDto<ResponseProductDto> searchByProductId(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return productService.searchByProductId(ProductDto.getSearchDto(productId), request);
    }

    @Operation(summary = "상품 목록 조회", description = "사용자 아이디를 입력하여 내가 등록한 상품 목록을 조회합니다.")
    @GetMapping(value = "/search", params = "userId")
    public ApiResponseDto<List<ResponseProductDto>> searchByUserId(@RequestParam("userId") String userId) {
        return productService.searchByUserId(ProductDto.getSearchDto(userId));
    }

    @Operation(summary = "상품 정보 수정", description = "상품 아이디를 입력하여 상품 정보를 수정합니다.")
    @PatchMapping("/update")
    public ApiResponseDto<ResponseProductDto> update(@RequestParam("productId") Long productId, @RequestBody @Valid ProductDto productDto, HttpServletRequest request) {
        return productService.update(ProductDto.getUpdateDto(productId, productDto), request);
    }

    @Operation(summary = "판매 중지", description = "상품 아이디를 입력하여 상품 상태를 판매중지로 변경합니다.")
    @PatchMapping("/sale-stop")
    public ApiResponseDto<ResponseProductDto> saleStop(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return productService.saleStop(ProductDto.getSearchDto(productId), request);
    }

    @Operation(summary = "판매 완료", description = "상품 아이디를 입력하여 상품 상태를 판매완료로 변경합니다.")
    @PatchMapping("/sold-out")
    public ApiResponseDto<ResponseProductDto> soldOut(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return productService.soldOut(ProductDto.getSearchDto(productId), request);
    }

    @Operation(summary = "상품 삭제", description = "상품 아이디를 입력하여 상품을 삭제합니다.")
    @DeleteMapping("/delete")
    public ApiResponseDto<ResponseProductDto> delete(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return productService.delete(ProductDto.getSearchDto(productId), request);
    }
}
