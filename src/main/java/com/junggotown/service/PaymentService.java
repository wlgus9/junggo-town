package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.domain.Orders;
import com.junggotown.domain.Payment;
import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.payment.PaymentDto;
import com.junggotown.dto.payment.ResponsePaymentDto;
import com.junggotown.dto.payment.ResponseVirtualAccountDto;
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
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Value("${toss.virtual-account-url}")
    private String VIRTUAL_ACCOUNT_URL;
    @Value("${toss.secret}")
    private String TOSS_SECRET_KEY;

    private static final String BANK = "20";
    private static final int VALID_HOURS = 1;

    public ApiResponseDto<ResponsePaymentDto> createVirtualAccount(Long productId, HttpServletRequest request) {
        String userId = jwtProvider.getUserId(request);

        Product product = productService.getProduct(productId, userId);
        Member member = memberService.getMember(userId); // 회원 정보 조회

        ResponseVirtualAccountDto responseVirtualAccountDto = requestVirtualAccount(PaymentDto.createPaymentDto(product, member)); // 가상계좌 발급

        Payment payment = Payment.getPaymentFromDto(responseVirtualAccountDto); // Payment Entity 생성
        String paymentId = paymentRepository.save(payment).getId().toString();

        Orders orders = Orders.getOrders(paymentId, productId, userId); // Orders Entity 생성
        ordersRepository.save(orders);

        return ApiResponseDto.response(ResponseMessage.VIRTUAL_ACCOUNT_CREATE_SUCCESS, ResponsePaymentDto.getCreateDto(orders, payment));
    }

    // 가상계좌 발급 요청
    public ResponseVirtualAccountDto requestVirtualAccount(PaymentDto paymentDto) {
        RestClient restClient = RestClient.builder()
                .baseUrl(VIRTUAL_ACCOUNT_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + TOSS_SECRET_KEY+"11")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return restClient.post()
                .body(paymentDto)
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
