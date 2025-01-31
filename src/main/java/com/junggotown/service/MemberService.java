package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.MemberDto;
import com.junggotown.global.ResponseMessage;
import com.junggotown.repository.MemberRepository;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    // private final BCryptPasswordEncoder passwordEncoder;

    public ApiResponseDto join(MemberDto memberDto) {
        boolean isSuccess;
        String message;

        Member member = Member.builder()
                        .userId(memberDto.getUserId())
                        // .userPw(passwordEncoder.encode(memberDto.getUserPw()))
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

        Member member = Member.builder()
                .userId(memberDto.getUserId())
                .userPw(memberDto.getUserPw())
                .build();

        try {
            boolean isExists = memberRepository.existsByUserId(member.getUserId());

            if(isExists) {
                isSuccess = true;
                message = ResponseMessage.LOGIN_SUCCESS;
            } else {
                isSuccess = false;
                message = ResponseMessage.LOGIN_FAIL;
            }
        } catch (Exception e) {
            isSuccess = false;
            message = ResponseMessage.LOGIN_FAIL;
        }

        return ApiResponseDto.builder()
                .success(isSuccess)
                .message(message)
                .build();
    }

    public MemberDto findByUserId(String userId) {
        Member member = memberRepository.findByUserId(userId);
        return MemberDto.builder()
                .userId(member.getUserId())
                .userPw(member.getUserPw())
                .build();
    }

}
