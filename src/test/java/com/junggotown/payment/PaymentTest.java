package com.junggotown.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.junggotown.TestUtil;
import com.junggotown.domain.Member;
import com.junggotown.domain.Product;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.dto.payment.PaymentDto;
import com.junggotown.dto.payment.ResponseVirtualAccountDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private String userId;
    private String token;

    private final String CREATE_URL = "/api/v1/payments/virtual-account/create";

    @Value("${toss.virtual-account-url}")
    private String VIRTUAL_ACCOUNT_URL;
    @Value("${toss.secret}")
    private String TOSS_SECRET_KEY;

    private static final Product product = Product.builder()
                                                .id(1L)
                                                .productName("아이폰15프로")
                                                .price(BigDecimal.valueOf(1000))
                                                .build();

    private static final Member member = Member.builder()
                                            .userId("test")
                                            .userName("홍길동")
                                            .build();

    @BeforeEach
    void 로그인() throws Exception {
        MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");

        memberService.join(memberDto);
        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

        userId = memberDto.getUserId();
        token = apiResponseDto.getData().getToken();
    }

    @Test
    void 가상계좌발급_API요청() {
        RestClient restClient = RestClient.builder()
                .baseUrl(VIRTUAL_ACCOUNT_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + TOSS_SECRET_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        ResponseVirtualAccountDto response = restClient.post()
                .body(PaymentDto.createPaymentDto(product, member))
                .retrieve()
                .body(ResponseVirtualAccountDto.class);

        log.info("response: {}", response);
        log.info("response: {}", response.getOrderId());
        log.info("response: {}", response.getVirtualAccount());
        log.info("response: {}", response.getVirtualAccount().getAccountNumber());
        log.info("response: {}", response.getVirtualAccount().getCustomerName());
    }

    @Test
    void 가상계좌발급_성공() throws Exception {
        saveProduct();

        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, CREATE_URL + "?productId=1", userId, token, HttpStatus.OK);

        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.VIRTUAL_ACCOUNT_CREATE_SUCCESS.getMessage());
    }

    @Test
    // secretKey에 임의의 문자열 더한 후 테스트
    void 가상계좌발급_실패() throws Exception {
        saveProduct();

        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, CREATE_URL + "?productId=1", userId, token, HttpStatus.UNAUTHORIZED);
        assertThat(response.get("code").asInt()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    void saveProduct() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("아이폰 15프로", "미개봉 새상품이에요.", BigDecimal.valueOf(100000));
        TestUtil.performPostRequestAndGetResponse(mockMvc, "/api/v1/products/create", userId, token, productDto, HttpStatus.OK);
    }
}
