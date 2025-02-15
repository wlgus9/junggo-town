package com.junggotown.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junggotown.domain.Member;
import com.junggotown.domain.Orders;
import com.junggotown.domain.Payment;
import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.dto.payment.ResponseVirtualAccountDto;
import com.junggotown.dto.payment.VirtualAccountDto;
import com.junggotown.dto.payment.WebHookDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.repository.OrdersRepository;
import com.junggotown.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Value("${toss.search-payment-url}")
    private String SEARCH_PAYMENT_URL;
    @Value("${toss.virtual-account-url}")
    private String VIRTUAL_ACCOUNT_URL;
    @Value("${toss.secret}")
    private String TOSS_SECRET_KEY;

    @Transactional
    public ApiResponseDto<ResponsePaymentDto> createVirtualAccount(Long productId, HttpServletRequest request) {
        String userId = jwtProvider.getUserId(request);

        Product product = productService.getProduct(productId, userId);
        Member member = memberService.getMember(userId); // 회원 정보 조회

        ResponseVirtualAccountDto responseVirtualAccountDto = requestVirtualAccount(VirtualAccountDto.createVirtualAccountDto(product, member)); // 가상계좌 발급

        Payment payment = Payment.getPaymentFromDto(responseVirtualAccountDto); // Payment Entity 생성
        UUID paymentId = paymentRepository.save(payment).getId();

        Orders orders = Orders.getOrders(paymentId, productId, userId); // Orders Entity 생성
        ordersRepository.save(orders);

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
        RestClient restClient = RestClient.builder()
                .baseUrl(SEARCH_PAYMENT_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + TOSS_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String response = restClient.get()
                .uri("/{paymentKey}", paymentKey)
                .retrieve()
                .body(String.class);

        try {
            String status = new ObjectMapper().readTree(response).get("status").asText();

            return ApiResponseDto.response(
                        ResponseMessage.SEARCH_PAYMENT_SUCCESS
                        , status.equals("DONE") ? ResponseMessage.PAYMENT_DONE.getMessage()
                            : status.equals("CANCELED") ? ResponseMessage.PAYMENT_CANCEL.getMessage()
                            :ResponseMessage.PAYMENT_WAIT_OR_FAIL.getMessage()
                    );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 가상계좌 발급 요청
    public ResponseVirtualAccountDto requestVirtualAccount(VirtualAccountDto virtualAccountDto) {
        RestClient restClient = RestClient.builder()
                .baseUrl(VIRTUAL_ACCOUNT_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + TOSS_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return restClient.post()
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
