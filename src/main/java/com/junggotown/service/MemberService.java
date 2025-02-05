package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.global.exception.member.MemberException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.message.ResponseMessage;
import com.junggotown.repository.MemberRepository;
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
    public ApiResponseDto<ResponseMemberDto> join(MemberDto memberDto) throws MemberException {
        Member member = Member.getMemberFromDto(memberDto, passwordEncoder);

        if(memberRepository.existsByUserId(member.getUserId())) {
            throw new MemberException(ResponseMessage.MEMBER_JOIN_DUPLICATE.getMessage());
        }

        Long id = memberRepository.save(member).getId();
        return ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_SUCCESS, ResponseMemberDto.getJoinDto(id));
    }

    public ApiResponseDto<ResponseMemberDto> login(MemberDto memberDto) throws MemberException {
        boolean passwordMatch = passwordEncoder.matches(memberDto.getUserPw(), memberRepository.findByUserId(memberDto.getUserId()).getUserPw());

        if(passwordMatch) {
            return ApiResponseDto.response(ResponseMessage.LOGIN_SUCCESS, ResponseMemberDto.getLoginDto(jwtUtil.createAccessToken(memberDto)));
        } else {
            throw new MemberException();
        }
    }
}
