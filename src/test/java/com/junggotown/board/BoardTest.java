package com.junggotown.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.board.BoardDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.service.BoardService;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class BoardTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private BoardService boardService;
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

    @Test
    void 글_작성_성공() throws Exception {
        BoardDto boardDto = BoardDto.getBoardDto("testTitle", "testDesc");
        String requestBody = objectMapper.writeValueAsString(boardDto);

        mockMvc.perform(post("/api/v1/boards/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()) // 200 응답 체크
                .andDo(print());
    }

    @Test
    void 글_작성_실패_MissingTokenException() throws Exception {
        BoardDto boardDto = BoardDto.getBoardDto("testTitle", "testDesc");
        String requestBody = objectMapper.writeValueAsString(boardDto);

        mockMvc.perform(post("/api/v1/boards/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("userId", userId))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 글_작성_실패_InvalidTokenException() throws Exception {
        BoardDto boardDto = BoardDto.getBoardDto("testTitle", "testDesc");
        String requestBody = objectMapper.writeValueAsString(boardDto);

        mockMvc.perform(post("/api/v1/boards/write")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("userId", userId)
                        .header("Authorization", "Bearer " + token + "invalid"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}
