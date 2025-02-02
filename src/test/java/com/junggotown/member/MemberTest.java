package com.junggotown.member;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.MemberDto;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() {
        MemberDto memberDto = MemberDto.builder()
                            .userId("테스트ID")
                            .userPw(passwordEncoder.encode("테스트PW"))
                            .userName("테스트")
                            .build();

        ApiResponseDto apiResponseDto = memberService.join(memberDto);

        assertThat(apiResponseDto.isSuccess()).isEqualTo(true);
    }

    @Test
    void 로그인() {
        MemberDto memberDto = MemberDto.builder()
                .userId("테스트ID")
                .userPw(passwordEncoder.encode("테스트PW"))
                .userName("테스트")
                .build();

        memberService.join(memberDto);

        ApiResponseDto apiResponseDto = memberService.login(memberDto);

        assertThat(apiResponseDto.isSuccess()).isEqualTo(true);
    }
}
