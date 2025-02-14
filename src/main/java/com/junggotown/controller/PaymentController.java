package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "결제", description = "결제 api")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "가상계좌 발급", description = "상품 아이디를 입력하여 가상계좌를 발급합니다.")
    @GetMapping("/virtual-account/create")
    public ApiResponseDto<ResponsePaymentDto> createVirtualAccount(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return paymentService.createVirtualAccount(productId, request);
    }
}
