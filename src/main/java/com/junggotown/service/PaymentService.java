package com.junggotown.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junggotown.domain.Orders;
import com.junggotown.domain.Payment;
import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.dto.payment.ResponseVirtualAccountDto;
import com.junggotown.dto.payment.VirtualAccountDto;
import com.junggotown.dto.payment.WebHookDto;
import com.junggotown.global.common.PaymentStatus;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrdersService ordersService;
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final RestClient restClient;

    @Value("${toss.endpoints.search}")
    private String SEARCH_PAYMENT_URL;
    @Value("${toss.endpoints.virtual-account}")
    private String VIRTUAL_ACCOUNT_URL;

    @Transactional
    public ApiResponseDto<ResponsePaymentDto> createVirtualAccount(Long productId, HttpServletRequest request) {
        String userId = jwtProvider.getUserId(request);
        String userName = jwtProvider.getUserName(request);

        Product product = productService.getProduct(productId, userId);

        // 가상계좌 발급
        ResponseVirtualAccountDto responseVirtualAccountDto = requestVirtualAccount(
                VirtualAccountDto.createVirtualAccountDto(product, userName)
        );

        Payment payment = Payment.getPaymentFromDto(responseVirtualAccountDto);
        UUID paymentId = paymentRepository.save(payment).getId();

        Orders orders = ordersService.saveOrder(paymentId, productId, userId);

        return ApiResponseDto.response(ResponseMessage.VIRTUAL_ACCOUNT_CREATE_SUCCESS, ResponsePaymentDto.getCreateDto(orders, payment));
    }

    @Transactional
    public void updatePaymentStatus(WebHookDto webHookDto) {
        String status = webHookDto.getStatus();

        paymentRepository.findByOrderId(webHookDto.getOrderId())
                .orElseThrow(() -> new CustomException(ResponseMessage.SEARCH_PAYMENT_FAIL))
                .changeStatus(status);

        log.info("updatePaymentStatus : {}", status);
    }

    public ApiResponseDto<String> searchPaymentStatus(String paymentKey) {
        String response = restClient.get()
                .uri(SEARCH_PAYMENT_URL + "/{paymentKey}", paymentKey)
                .retrieve()
                .body(String.class);

        try {
            String status = new ObjectMapper().readTree(response).get("status").asText();
            return ApiResponseDto.response(ResponseMessage.SEARCH_PAYMENT_SUCCESS, getStatusMessage(status));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getStatusMessage(String status) {
        if(status.equals(PaymentStatus.DONE.getStatus())) {
            return ResponseMessage.PAYMENT_DONE.getMessage();
        } else if(status.equals(PaymentStatus.CANCELED.getStatus())) {
            return ResponseMessage.PAYMENT_CANCEL.getMessage();
        } else {
            return ResponseMessage.PAYMENT_WAIT_OR_FAIL.getMessage();
        }
    }

    // 가상계좌 발급 요청
    public ResponseVirtualAccountDto requestVirtualAccount(VirtualAccountDto virtualAccountDto) {
        return restClient.post()
                .uri(VIRTUAL_ACCOUNT_URL)
                .body(virtualAccountDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new CustomException(
                            response.getStatusCode().value()
                            , new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8)
                    );
                })
                .body(ResponseVirtualAccountDto.class);
    }
}
