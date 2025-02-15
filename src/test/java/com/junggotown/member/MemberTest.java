package com.junggotown.member;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.global.exception.CustomException;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.yml") //test용 properties 파일 설정
@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired BCryptPasswordEncoder passwordEncoder;
    @Autowired MemberService memberService;

    @Test
    void 회원가입_성공() {
        MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.join(memberDto);

        assertThat(apiResponseDto.getHttpStatus().value()).isEqualTo(200);
    }

    @Test
    void 회원가입_실패() {
        assertThrows(CustomException.class, () -> {
            MemberDto memberDto1 = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
            memberService.join(memberDto1);

            MemberDto memberDto2 = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
            memberService.join(memberDto2);
        });
    }

    @Test
    void 로그인_성공() {
        MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
        memberService.join(memberDto);

        ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

        assertThat(apiResponseDto.getHttpStatus().value()).isEqualTo(200);
    }

    @Test
    void 로그인_실패() {
        assertThrows(CustomException.class, () -> {
            MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
            memberService.join(memberDto);

            MemberDto loginDto = MemberDto.getLoginDto("테스트ID", "잘못된PW");
            memberService.login(loginDto);
        });
    }
}
