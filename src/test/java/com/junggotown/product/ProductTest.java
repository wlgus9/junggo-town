package com.junggotown.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.dto.product.ProductDto;
import com.junggotown.global.message.ResponseMessage;
import com.junggotown.service.MemberService;
import com.junggotown.service.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.yml") //test용 yml 파일 설정
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductService productService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private MemberService memberService;

    private String userId;
    private String token;

    @BeforeEach
    void 로그인() {
        MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");

        memberService.join(memberDto);
        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

        userId = memberDto.getUserId();
        token = apiResponseDto.getData().getToken();
    }

    // @BeforeEach
    // void AUTO_INCREMENT_초기화() {
    //     em.createNativeQuery("ALTER TABLE PRODUCT AUTO_INCREMENT = 1").executeUpdate();
    // }

    @Test
    void 상품_등록_성공() throws Exception {
        ProductDto productDto1 = ProductDto.getCreateDto("testName1", "testDesc1", BigDecimal.valueOf(10000));
        ProductDto productDto2 = ProductDto.getCreateDto("testName2", "testDesc2", BigDecimal.valueOf(20000));
        ProductDto productDto3 = ProductDto.getCreateDto("testName3", "testDesc3", BigDecimal.valueOf(30000));

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto1))
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto2))
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto3))
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 상품_등록_실패_MissingTokenException() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("testName", "testDesc", BigDecimal.valueOf(10000));
        String requestBody = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/v1/products/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("userId", userId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 상품_등록_실패_InvalidTokenException() throws Exception {
        ProductDto productDto = ProductDto.getCreateDto("testName", "testDesc", BigDecimal.valueOf(10000));
        String requestBody = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(post("/api/v1/products/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token + "invalid"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void 상품_조회_ProductId() throws Exception {
        상품_등록_성공();

        mockMvc.perform(get("/api/v1/products/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", "1")
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 상품_조회_UserId() throws Exception {
        상품_등록_성공();

        mockMvc.perform(get("/api/v1/products/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", userId)
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 상품_수정_성공() throws Exception {
        상품_등록_성공();

        ProductDto updateDto = ProductDto.getCreateDto("updateName", "updateDesc", BigDecimal.valueOf(20000));

        mockMvc.perform(patch("/api/v1/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 상품_수정_실패_다른사람상품() throws Exception {
        상품_등록_성공();

        ProductDto updateDto = ProductDto.getCreateDto("updateName", "updateDesc", BigDecimal.valueOf(20000));

        String response = mockMvc.perform(patch("/api/v1/products/update/4")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(updateDto))
                                    .header("userId", userId)
                                    .header("Authorization", "Bearer " + token))
                            .andDo(print())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }

    @Test
    void 상품_삭제_성공() throws Exception {
        상품_등록_성공();

        mockMvc.perform(delete("/api/v1/products/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 상품_삭제_실패_다른사람상품() throws Exception {
        상품_등록_성공();

        String response = mockMvc.perform(delete("/api/v1/products/delete/4")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("userId", userId)
                                    .header("Authorization", "Bearer " + token))
                            .andDo(print())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        assertThat(jsonNode.get("message").asText()).isEqualTo(ResponseMessage.PRODUCT_IS_NOT_YOURS.getMessage());
    }
}
