package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtUtil;

    @Transactional
    public ApiResponseDto<ResponseMemberDto> join(MemberDto memberDto) {
        Member member = Member.getMemberFromDto(memberDto, passwordEncoder);

        if(memberRepository.existsByUserId(member.getUserId())) {
            throw new CustomException(ResponseMessage.MEMBER_JOIN_DUPLICATE);
        }

        Long id = memberRepository.save(member).getId();
        return ApiResponseDto.response(ResponseMessage.MEMBER_JOIN_SUCCESS, ResponseMemberDto.getJoinDto(id));
    }

    public ApiResponseDto<ResponseMemberDto> login(MemberDto memberDto) {
        Optional<Member> member = memberRepository.findByUserId(memberDto.getUserId());

        boolean passwordMatch = passwordEncoder.matches(memberDto.getUserPw(), member.map(Member::getUserPw)
                        .orElseThrow(() -> new CustomException(ResponseMessage.LOGIN_FAIL)));

        if(passwordMatch) {
            return ApiResponseDto.response(ResponseMessage.LOGIN_SUCCESS
                    , ResponseMemberDto.getLoginDto(jwtUtil.createAccessToken(member
                            .orElseThrow(() -> new CustomException(ResponseMessage.MEMBER_IS_NOT_EXISTS))
                    ))
            );
        } else {
            throw new CustomException(ResponseMessage.LOGIN_FAIL);
        }
    }
}
