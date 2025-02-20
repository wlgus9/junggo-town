package com.junggotown.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.junggotown.TestUtil;
import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.dto.payment.ResponseVirtualAccountDto;
import com.junggotown.dto.payment.VirtualAccountDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml") //test용 yml 파일 설정
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PaymentTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private MemberService memberService;
    @Autowired private RestClient restClient;

    private String userId;
    private String token;

    private final String CREATE_URL = "/api/v1/payments/virtual-account/create";
    private final String SEARCH_URL = "/api/v1/payments/status";

    @Value("${toss.endpoints.search}")
    private String SEARCH_PAYMENT_URL;
    @Value("${toss.endpoints.virtual-account}")
    private String VIRTUAL_ACCOUNT_URL;

    private static final Product product = Product.builder()
                                                .id(1L)
                                                .productName("맥북 M1")
                                                .price(BigDecimal.valueOf(100000))
                                                .build();

    private static final String userName = "홍길동";

    @BeforeEach
    @DisplayName("로그인 성공")
    void login() throws Exception {
        MemberDto memberDto = MemberDto.getMemberDto(
                "테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678"
        );

        memberService.join(memberDto);
        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

        userId = memberDto.getUserId();
        token = apiResponseDto.getData().getToken();
    }

    @Nested
    @DisplayName("가상계좌 발급할 때")
    class VirtualAccount {
        @Test
        @DisplayName("성공이면 응답 메세지가 ResponseMessage.VIRTUAL_ACCOUNT_CREATE_SUCCESS와 같다")
        void virtualAccountSuccess() throws Exception {
            saveProduct();

            JsonNode response = TestUtil.performGetRequestAndGetResponse(
                    mockMvc, CREATE_URL + "?productId=1", userId, token, HttpStatus.OK
            );

            assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.VIRTUAL_ACCOUNT_CREATE_SUCCESS.getMessage());
        }

        // Service계층에서 secretKey에 임의의 문자열 더한 후 테스트
        @Test
        @DisplayName("유효하지 않은 시크릿키면 HTTP 상태 코드가 401이 반환된다")
        void virtualAccountFailIsInvalidSecretKey() throws Exception {
            saveProduct();

            JsonNode response = TestUtil.performGetRequestAndGetResponse(
                    mockMvc, CREATE_URL + "?productId=1", userId, token, HttpStatus.UNAUTHORIZED
            );

            assertThat(response.get("code").asInt()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Nested
    @DisplayName("결제상태 조회할 때")
    class Search {
        @Test
        @DisplayName("성공이면 응답 메세지가 ResponseMessage.SEARCH_PAYMENT_SUCCESS와 같다")
        void searchPaymentSuccess() throws Exception {
            String pamentKey = virtualAccountRequestAPI();

            JsonNode response = TestUtil.performGetRequestAndGetResponse(
                    mockMvc, SEARCH_URL + "?paymentKey="+pamentKey, userId, token, HttpStatus.OK
            );

            assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.SEARCH_PAYMENT_SUCCESS.getMessage());
        }

        @Test
        @DisplayName("유효하지 않은 paymentKey면 HTTP 상태 코드 404가 반환된다")
        void searchPaymentFailIsInvalidPaymentKey() throws Exception {
            String pamentKey = virtualAccountRequestAPI();

            JsonNode response = TestUtil.performGetRequestAndGetResponse(
                    mockMvc, SEARCH_URL + "?paymentKey="+pamentKey+"invalid", userId, token, HttpStatus.NOT_FOUND
            );

            assertThat(response.get("code").asInt()).isEqualTo(404);
        }
    }

    String virtualAccountRequestAPI() {
        ResponseVirtualAccountDto response = restClient.post()
                .uri(VIRTUAL_ACCOUNT_URL)
                .body(VirtualAccountDto.createVirtualAccountDto(product, userName))
                .retrieve()
                .body(ResponseVirtualAccountDto.class);

        return response.getPaymentKey();
    }

    void searchPaymentRequestAPI() {
        ResponseVirtualAccountDto response = restClient.get()
                .uri(SEARCH_PAYMENT_URL+"/{paymentKey}", "")
                .retrieve()
                .body(ResponseVirtualAccountDto.class);

        log.info("status : {}", response.getStatus());
        log.info("response: {}", response.getOrderId());
        log.info("response: {}", response.getPaymentKey());
        log.info("response: {}", response.getVirtualAccount().getAccountNumber());
        log.info("response: {}", response.getVirtualAccount().getCustomerName());
    }

    void saveProduct() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("아이폰 15프로", "미개봉 새상품이에요.", BigDecimal.valueOf(100000));
        TestUtil.performPostRequestAndGetResponse(mockMvc, "/api/v1/products/create", userId, token, productDto, HttpStatus.OK);
    }
}
