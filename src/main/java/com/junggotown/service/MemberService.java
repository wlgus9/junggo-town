package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.MemberDto;
import com.junggotown.global.JwtProvider;
import com.junggotown.global.ResponseMessage;
import com.junggotown.repository.MemberRepository;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtUtil;

    @Transactional
    public ApiResponseDto join(MemberDto memberDto) {
        boolean isSuccess;
        String message;

        Member member = Member.builder()
                        .userId(memberDto.getUserId())
                        .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                        .userName(memberDto.getUserName())
                        .userTelno(memberDto.getUserTelno())
                        .build();

        boolean isExists = memberRepository.existsByUserId(member.getUserId());

        if(isExists) {
            return ApiResponseDto.builder()
                    .success(false)
                    .message(ResponseMessage.MEMBER_CREATE_FAIL_DUPLICATE)
                    .build();
        }

        try {
            memberRepository.save(member);
            isSuccess = true;
            message = ResponseMessage.MEMBER_CREATE_SUCCESS;
        } catch (PersistenceException e) {
            isSuccess = false;
            message = ResponseMessage.DATABASE_ERROR;
        } catch (Exception e) {
            isSuccess = false;
            message = ResponseMessage.MEMBER_CREATE_FAIL;
        }

        return ApiResponseDto.builder()
                .success(isSuccess)
                .message(message)
                .build();
    }

    public ApiResponseDto login(MemberDto memberDto) {
        boolean isSuccess;
        String message;
        String token = "";

        try {
            Member loginMember = Member.builder()
                                    .userPw(memberRepository.findByUserId(memberDto.getUserId()).getUserPw())
                                    .build();

            if(passwordEncoder.matches(memberDto.getUserPw(), loginMember.getUserPw())) {
                isSuccess = true;
                message = ResponseMessage.LOGIN_SUCCESS;
                token = jwtUtil.createAccessToken(memberDto); // 로그인 성공 시 토큰 발급
            } else {
                isSuccess = true;
                message = ResponseMessage.LOGIN_FAIL;
            }
        } catch (Exception e) {
            isSuccess = false;
            message = ResponseMessage.LOGIN_FAIL;
        }

        return ApiResponseDto.builder()
                .success(isSuccess)
                .message(message)
                .token(token)
                .build();
    }
}
