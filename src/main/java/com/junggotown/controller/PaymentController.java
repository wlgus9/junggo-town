package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.dto.payment.WebHookDto;
import com.junggotown.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "결제승인", description = "가상계좌의 입금한 결제내역을 승인합니다.")
    @GetMapping("/confirm")
    public ApiResponseDto<ResponsePaymentDto> confirmPayment(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return null;
    }

    @Operation(summary = "웹훅", description = "가상계좌 웹훅의 엔드포인트입니다.")
    @PostMapping("/hook")
    public HttpStatus webHook(@RequestBody WebHookDto webHookDto) {
        log.info("getOrderId : {}", webHookDto.getOrderId());
        log.info("getStatus : {}", webHookDto.getStatus());

        return HttpStatus.OK;
    }
}
