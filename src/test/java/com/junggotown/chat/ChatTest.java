package com.junggotown.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.junggotown.TestUtil;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.chat.ChatDto;
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
public class ChatTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private MemberService memberService;
    @Autowired private JdbcTemplate jdbcTemplate;

    private String registUserId1;
    private String registUserToken1;
    private String registUserId2;
    private String registUserToken2;
    private String consumerUserId;
    private String consumerUserToken;

    private final String CREATE_URL = "/api/v1/products/create";
    private final String SEND_URL = "/api/v1/chat/send";
    private final String SEARCH_URL = "/api/v1/chat/search?productId=";
    private final String SEARCH_ALL_URL = "/api/v1/chat/search-all";

    @BeforeEach
    void 로그인() {
        MemberDto memberDto1 = MemberDto.getMemberDto("등록자1ID", passwordEncoder.encode("등록자1PW"), "등록자1", "010-1234-5678");
        MemberDto memberDto2 = MemberDto.getMemberDto("등록자2ID", passwordEncoder.encode("등록자2PW"), "등록자2", "010-1234-5678");
        MemberDto memberDto3 = MemberDto.getMemberDto("구매자ID", passwordEncoder.encode("구매자PW"), "구매자", "010-2345-6789");

        memberService.join(memberDto1);
        memberService.join(memberDto2);
        memberService.join(memberDto3);

        ApiResponseDto<ResponseMemberDto> apiResponseDto1 = memberService.login(memberDto1);
        registUserId1 = memberDto1.getUserId();
        registUserToken1 = apiResponseDto1.getData().getToken();

        ApiResponseDto<ResponseMemberDto> apiResponseDto2 = memberService.login(memberDto2);
        registUserId2 = memberDto2.getUserId();
        registUserToken2 = apiResponseDto2.getData().getToken();

        ApiResponseDto<ResponseMemberDto> apiResponseDto3 = memberService.login(memberDto3);
        consumerUserId = memberDto3.getUserId();
        consumerUserToken = apiResponseDto3.getData().getToken();
    }

    @BeforeEach
    void autoIncrementReset() {
        jdbcTemplate.execute("ALTER TABLE product ALTER COLUMN id RESTART WITH 1;");
        jdbcTemplate.execute("ALTER TABLE chat ALTER COLUMN id RESTART WITH 1;");
    }

    @Test
    void 채팅_전송_성공() throws Exception {
        saveProduct();

        ChatDto chatDto1_1 = ChatDto.getCreateDto(1L, consumerUserId, registUserId1, "상품 아직 있나요?");
        ChatDto chatDto1_2 = ChatDto.getCreateDto(1L, registUserId1, consumerUserId, "네 있어요.");
        ChatDto chatDto1_3 = ChatDto.getCreateDto(1L, consumerUserId, registUserId1, "구매하고 싶어요.");

        ChatDto chatDto2_1 = ChatDto.getCreateDto(3L, consumerUserId, registUserId2, "안녕하세요 구매하고 싶어요.");
        ChatDto chatDto2_2 = ChatDto.getCreateDto(3L, registUserId2, consumerUserId, "죄송해요 팔렸어요.");
        ChatDto chatDto2_3 = ChatDto.getCreateDto(3L, consumerUserId, registUserId2, "네 ㅠㅠ");

        JsonNode response1_1 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto1_1, HttpStatus.OK);
        JsonNode response1_2 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, registUserId1, registUserToken1, chatDto1_2, HttpStatus.OK);
        JsonNode response1_3 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto1_3, HttpStatus.OK);

        JsonNode response2_1 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto2_1, HttpStatus.OK);
        JsonNode response2_2 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, registUserId2, registUserToken2, chatDto2_2, HttpStatus.OK);
        JsonNode response2_3 = TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto2_3, HttpStatus.OK);

        assertThat(response1_1.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());
        assertThat(response1_2.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());
        assertThat(response1_3.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());

        assertThat(response2_1.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());
        assertThat(response2_2.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());
        assertThat(response2_3.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEND_SUCCESS.getMessage());
    }

    @Test
    void 채팅_조회_성공() throws Exception {
        saveChat();

        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_URL+"1", consumerUserId, consumerUserToken, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEARCH_SUCCESS.getMessage());
    }

    @Test
    void 채팅_조회_실패() throws Exception {
        saveChat();

        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_URL+"4", consumerUserId, consumerUserToken, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.CHAT_IS_EMPTY.getMessage());
    }

    @Test
    void 채팅_전체_조회_성공() throws Exception{
        saveChat();

        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_ALL_URL, consumerUserId, consumerUserToken, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.CHAT_SEARCH_SUCCESS.getMessage());
    }

    @Test
    void 채팅_전체_조회_실패() throws Exception{
        JsonNode response = TestUtil.performGetRequestAndGetResponse(mockMvc, SEARCH_ALL_URL, consumerUserId, consumerUserToken, HttpStatus.OK);
        assertThat(response.get("message").asText()).isEqualTo(ResponseMessage.CHAT_IS_EMPTY.getMessage());
    }

    void saveProduct() throws Exception {
        ProductDto productDto1 = ProductDto.getCreateDto("testName1", "testDesc1", BigDecimal.valueOf(10000));
        ProductDto productDto2 = ProductDto.getCreateDto("testName2", "testDesc2", BigDecimal.valueOf(20000));
        ProductDto productDto3 = ProductDto.getCreateDto("testName3", "testDesc3", BigDecimal.valueOf(30000));

        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, registUserId1, registUserToken1, productDto1, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, registUserId1, registUserToken1, productDto2, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, CREATE_URL, registUserId2, registUserToken2, productDto3, HttpStatus.OK);
    }

    void saveChat() throws Exception {
        saveProduct();

        ChatDto chatDto1_1 = ChatDto.getCreateDto(1L, consumerUserId, registUserId1, "상품 아직 있나요?");
        ChatDto chatDto1_2 = ChatDto.getCreateDto(1L, registUserId1, consumerUserId, "네 있어요.");
        ChatDto chatDto1_3 = ChatDto.getCreateDto(1L, consumerUserId, registUserId1, "구매하고 싶어요.");

        ChatDto chatDto2_1 = ChatDto.getCreateDto(3L, consumerUserId, registUserId2, "안녕하세요 구매하고 싶어요.");
        ChatDto chatDto2_2 = ChatDto.getCreateDto(3L, registUserId2, consumerUserId, "죄송해요 팔렸어요.");
        ChatDto chatDto2_3 = ChatDto.getCreateDto(3L, consumerUserId, registUserId2, "네 ㅠㅠ");

        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto1_1, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, registUserId1, registUserToken1, chatDto1_2, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto1_3, HttpStatus.OK);

        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto2_1, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, registUserId2, registUserToken2, chatDto2_2, HttpStatus.OK);
        TestUtil.performPostRequestAndGetResponse(mockMvc, SEND_URL, consumerUserId, consumerUserToken, chatDto2_3, HttpStatus.OK);
    }
}
