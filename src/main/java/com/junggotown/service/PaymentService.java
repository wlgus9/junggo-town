package com.junggotown.service;

import com.junggotown.domain.Product;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ProductService productService;

    @Value("${toss.virtual-account-url}")
    private String VIRTUAL_ACCOUNT_URL;
    @Value("${toss.secret}")
    private String TOSS_SECRET_KEY;

    // 가상계좌 발급 로직
    public void getVirtualAccount(Long productId, HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + TOSS_SECRET_KEY);

        // 요청 바디 데이터
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", 500000);
        requestBody.put("orderId", "order_2345");
        requestBody.put("orderName", "아이폰 15프로");
        requestBody.put("customerName", "홍길동");
        requestBody.put("bank", "토스"); // 가상계좌 발급 은행
        requestBody.put("validHours", 1); // 가상계좌 유효 시간(기본값:168(7일))

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(VIRTUAL_ACCOUNT_URL, HttpMethod.POST, requestEntity, String.class);

        // 응답 출력
        log.info("Response: {}", response.getBody());
    }

    public Product getProductInfo(Long productId, HttpServletRequest request) {
        // 상품 정보 조회 로직
        return productService.getProductInfo(productId, request);
    }

}
