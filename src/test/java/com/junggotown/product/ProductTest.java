package com.junggotown.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.junggotown.TestUtil;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml") //test용 yml 파일 설정
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private MemberService memberService;
    @Autowired private JdbcTemplate jdbcTemplate;

    private String userId;
    private String token;

    private final String CREATE_URL = "/api/v1/products/create";
    private final String SEARCH_URL = "/api/v1/products/search?";
    private final String UPDATE_URL = "/api/v1/products/update?productId=";
    private final String SALESTOP_URL = "/api/v1/products/sale-stop?productId=";
    private final String SOLDOUT_URL = "/api/v1/products/sold-out?productId=";
    private final String DELETE_URL = "/api/v1/products/delete?productId=";

    @BeforeEach
    void 로그인() throws Exception {
        MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");

        memberService.join(memberDto);
        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

        userId = memberDto.getUserId();
        token = apiResponseDto.getData().getToken();
    }

    @BeforeEach
    void autoIncrementReset() {
        jdbcTemplate.execute("ALTER TABLE product ALTER COLUMN id RESTART WITH 1;");
    }

    @Test
    void 상품_등록_성공() throws Exception {
        ProductDto productDto1 = ProductDto.getCreateDto("testName1", "testDesc1", BigDecimal.valueOf(10000));
        ProductDto productDto2 = ProductDto.getCreateDto("testName2", "testDesc2", BigDecimal.valueOf(20000));
        ProductDto productDto3 = ProductDto.getCreateDto("testName3", "testDesc3", BigDecimal.valueOf(30000));

        JsonNode response1 = TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto1, HttpStatus.OK);
        assertThat(response1.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_CREATE_SUCCESS.getMessage());

        JsonNode response2 = TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto2, HttpStatus.OK);
        assertThat(response2.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_CREATE_SUCCESS.getMessage());

        JsonNode response3 = TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto3, HttpStatus.OK);
        assertThat(response3.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_CREATE_SUCCESS.getMessage());
    }

    @Test
    void 상품_등록_실패_MissingTokenException() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("testName", "testDesc", BigDecimal.valueOf(10000));

        JsonNode response = TestUtil.performMissingTokenRequestAndGetResponse(mockMvc, CREATE_URL, userId, productDto, HttpStatus.UNAUTHORIZED);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.MISSING_TOKEN.getMessage());
    }

    @Test
    void 상품_등록_실패_InvalidTokenException() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("testName", "testDesc", BigDecimal.valueOf(10000));

        JsonNode response = TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token + "invalid", productDto, HttpStatus.FORBIDDEN);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.INVALID_TOKEN.getMessage());
    }

    @Test
    void 상품_조회_ProductId() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_URL+"productId=1", userId, token, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_SEARCH_SUCCESS.getMessage());
    }

    @Test
    void 상품_조회_UserId() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_URL+"userId="+userId, userId, token, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_SEARCH_SUCCESS.getMessage());
    }

    @Test
    void 상품_수정_성공() throws Exception {
        saveProduct();
        ProductDto updateDto = ProductDto.getCreateDto("updateName", "updateDesc", BigDecimal.valueOf(20000));

        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, UPDATE_URL+"1", userId, token, updateDto, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_UPDATE_SUCCESS.getMessage());
    }

    @Test
    void 상품_수정_실패_다른사람상품() throws Exception {
        saveProduct();
        ProductDto updateDto = ProductDto.getCreateDto("updateName", "updateDesc", BigDecimal.valueOf(20000));

        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, UPDATE_URL+"4", userId, token, updateDto, HttpStatus.BAD_REQUEST);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }

    @Test
    void 판매중지_성공() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, SALESTOP_URL+"1", userId, token, null, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_SALESTOP_SUCCESS.getMessage());
    }

    @Test
    void 판매중지_실패_다른사람상품() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, SALESTOP_URL+"4", userId, token, null, HttpStatus.BAD_REQUEST);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }

    @Test
    void 판매완료_성공() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, SOLDOUT_URL+"1", userId, token, null, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_SOLDOUT_SUCCESS.getMessage());
    }

    @Test
    void 판매완료_실패_다른사람상품() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performPatchRequestAndGetResponse(mockMvc, SOLDOUT_URL+"4", userId, token, null, HttpStatus.BAD_REQUEST);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }

    @Test
    void 상품_삭제_성공() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performDeleteRequestAndGetResponse(mockMvc, DELETE_URL+"1", userId, token, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_DELETE_SUCCESS.getMessage());
    }

    @Test
    void 상품_삭제_실패_다른사람상품() throws Exception {
        saveProduct();
        JsonNode response = TestUtil.performDeleteRequestAndGetResponse(mockMvc, DELETE_URL+"4", userId, token, HttpStatus.BAD_REQUEST);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }

    void saveProduct() throws Exception {
        ProductDto productDto1 = ProductDto.getCreateDto("testName1", "testDesc1", BigDecimal.valueOf(10000));
        ProductDto productDto2 = ProductDto.getCreateDto("testName2", "testDesc2", BigDecimal.valueOf(20000));
        ProductDto productDto3 = ProductDto.getCreateDto("testName3", "testDesc3", BigDecimal.valueOf(30000));

        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto1, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto2, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, userId, token, productDto3, HttpStatus.OK);
    }
}
