package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 api")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "아이디(id)와 비밀번호(password)를 입력하여 회원가입합니다.")
    @PostMapping("/join")
    public ApiResponseDto<ResponseMemberDto> join(@RequestBody @Valid MemberDto memberDto) {
        return memberService.join(memberDto);
    }

    @Operation(summary = "로그인", description = "아이디(id)와 비밀번호(password)를 입력하여 로그인 합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<ResponseMemberDto>> login(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw) {
        return memberService.login(MemberDto.getLoginDto(userId, userPw));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return memberService.logout(refreshToken);
    }
}
