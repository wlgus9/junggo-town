package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.MemberDto;
import com.junggotown.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 api")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "아이디(id)와 비밀번호(password)를 입력하여 회원가입합니다."
            // , parameters = {@Parameter(name = "userId"), @Parameter(name = "userPw"), @Parameter(name = "userName"), @Parameter(name = "userTelno")}
            , responses = {
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"
                    , content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApiResponseDto.class
                            )
                        )
                    ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"
                    , content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponse.class
                        )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "회원 가입 실패"
                    , content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                        )
            )
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponseDto> join(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입에 필요한 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.class)
                    )) MemberDto memberDto) {
        ApiResponseDto apiResponseDto = memberService.join(memberDto);

        return ResponseEntity.status(apiResponseDto.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseDto);
    }

    @Operation(summary = "로그인", description = "아이디(id)와 비밀번호(password)를 입력하여 로그인 합니다."
            , responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "500", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ApiResponseDto login(
            @RequestParam("userId") @Parameter(description = "아이디(id)", required = true) String userId
            , @RequestParam("userPw") @Parameter(description = "비밀번호(password)", required = true) String userPw) {

        MemberDto memberDto = MemberDto.builder()
                .userId(userId)
                .userPw(userPw)
                .build();

        return memberService.login(memberDto);
    }

    // @PostMapping("/update")
    // public String update(@RequestParam("userId") String userId, @RequestParam("userPw") String userPw, @RequestParam("originId") String originId, Model model) throws UnsupportedEncodingException {
    //     memberService.updateMemberByUserIdAndUserPw(userId, userPw, originId);
    //
    //     return "redirect:/boards?userId=" + URLEncoder.encode(userId, "UTF-8");
    // }
    //
    // @GetMapping("/delete")
    // public String delete(@RequestParam("userId") String userId) {
    //     memberService.deleteMemberByUserId(userId);
    //
    //     return "member";
    // }
}
