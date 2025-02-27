package com.junggotown.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junggotown.controller.MemberController;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private static final MemberDto memberDto = MemberDto.getMemberDto(
            "testId", "qwer1234@", "홍길동", "010-1234-5678"
    );

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void joinSuccess() throws Exception {
        when(memberService.join(any(MemberDto.class))).thenReturn(ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_SUCCESS));

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessage.MEMBER_JOIN_SUCCESS.getMessage()));

        verify(memberService, times(1)).join(any(MemberDto.class));
    }

    @Test
    @DisplayName("아이디가 중복 시 회원가입 실패")
    void joinFailDuplicateUserId() throws Exception {
        when(memberService.join(any(MemberDto.class))).thenReturn(ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_SUCCESS));

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessage.MEMBER_JOIN_SUCCESS.getMessage()));

        when(memberService.join(any(MemberDto.class))).thenReturn(ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_DUPLICATE));

        mockMvc.perform(post("/api/v1/members/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(memberDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessage.MEMBER_JOIN_DUPLICATE.getMessage()));


        verify(memberService, times(2)).join(any(MemberDto.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {
        when(memberService.login(any(MemberDto.class))).thenReturn(ResponseEntity.ok(ApiResponseDto.response(ResponseMessage.LOGIN_SUCCESS)));

        mockMvc.perform(post("/api/v1/members/login")
                        .param("userId", memberDto.getUserId())
                        .param("userPw", memberDto.getUserPw())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_SUCCESS.getMessage()));


        verify(memberService, times(1)).login(any(MemberDto.class));
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFailNotMatchPassword() throws Exception {
        when(memberService.login(any(MemberDto.class))).thenThrow(CustomException.class);

        mockMvc.perform(post("/api/v1/members/login")
                        .param("userId", memberDto.getUserId())
                        .param("userPw", memberDto.getUserPw()+"invalid")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_FAIL.getMessage()));


        verify(memberService, times(1)).login(any(MemberDto.class));
    }
}
