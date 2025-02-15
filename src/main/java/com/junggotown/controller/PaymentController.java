package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.dto.payment.WebHookDto;
import com.junggotown.service.PaymentService;
import io.swagger.v3.oas.annotations.Hidden;
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

    @Operation(summary = "웹훅", description = "가상계좌 웹훅의 엔드포인트입니다. 결제 상태를 업데이트합니다.")
    @Hidden
    @PostMapping("/hook")
    public HttpStatus webHook(@RequestBody WebHookDto webHookDto) {
        paymentService.updatePaymentStatus(webHookDto);
        return HttpStatus.OK;
    }

    @Operation(summary = "결제상태 조회", description = "paymentKey를 입력하여 해당 결제의 상태를 조회합니다.")
    @GetMapping("/status")
    public ApiResponseDto<String> searchPaymentStatus(@RequestParam("paymentKey") String paymentKey) {
        return paymentService.searchPaymentStatus(paymentKey);
    }
}
