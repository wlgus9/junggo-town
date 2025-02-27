package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.jwt.RefreshTokenService;
import com.junggotown.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public ApiResponseDto<ResponseMemberDto> join(MemberDto memberDto) {
        Member member = Member.getMemberFromDto(memberDto, passwordEncoder);

        if(memberRepository.existsByUserId(member.getUserId())) {
            throw new CustomException(ResponseMessage.MEMBER_JOIN_DUPLICATE);
        }

        Long id = memberRepository.save(member).getId();
        return ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_SUCCESS, ResponseMemberDto.getJoinDto(id));
    }

    public ResponseEntity<ApiResponseDto<ResponseMemberDto>> login(MemberDto memberDto) {
        String userId = memberDto.getUserId();

        Member member = memberRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(ResponseMessage.MEMBER_IS_NOT_EXISTS));

        boolean passwordMatch = passwordEncoder.matches(memberDto.getUserPw(), member.getUserPw());

        if(passwordMatch) {
            String accessToken = jwtUtil.createAccessToken(member);
            String refreshToken = jwtUtil.createRefreshToken(member);

            // Redis에 리프레시 토큰 저장
            refreshTokenService.saveRefreshToken(userId, refreshToken);

            // 리프레시 토큰을 httpOnly 쿠키로 설정
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7일
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", refreshCookie.toString())
                    .body(ApiResponseDto.response(ResponseMessage.LOGIN_SUCCESS
                            , ResponseMemberDto.getLoginDto(accessToken))
                    );
        } else {
            throw new CustomException(ResponseMessage.LOGIN_FAIL);
        }
    }

    public ResponseEntity<String> logout(String refreshToken) {
        if (refreshToken != null) {
            String userId = jwtUtil.parseClaims(refreshToken).get("userId").toString();
            refreshTokenService.deleteRefreshToken(userId);
        }

        // 쿠키 삭제
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                                                    .httpOnly(true)
                                                    .secure(true)
                                                    .path("/")
                                                    .maxAge(0)
                                                    .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(ResponseMessage.LOGOUT_SUCCESS.getMessage());
    }
}
