package com.junggotown.member;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.member.MemberDto;
import com.junggotown.dto.member.ResponseMemberDto;
import com.junggotown.global.exception.CustomException;
import com.junggotown.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource("classpath:application-test.yml") //test용 properties 파일 설정
@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired BCryptPasswordEncoder passwordEncoder;
    @Autowired MemberService memberService;

    @Nested
    @DisplayName("회원가입 할 때")
    class Join {
        @Test
        @DisplayName("성공이면 HTTP 상태 코드 200을 반환한다")
        void joinSuccess() {
            MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
            ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.join(memberDto);

            assertThat(apiResponseDto.getHttpStatus().value()).isEqualTo(200);
        }

        @Test
        @DisplayName("아이디 중복인 경우 CustomException이 발생한다")
        void joinFailDuplicate() {
            assertThrows(CustomException.class, () -> {
                MemberDto memberDto1 = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
                memberService.join(memberDto1);

                MemberDto memberDto2 = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
                memberService.join(memberDto2);
            });
        }
    }

    @Nested
    @DisplayName("로그인 할 때")
    class Login {
        @Test
        @DisplayName("성공이면 HTTP 상태 코드 200을 반환한다")
        void loginSuccess() {
            MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
            memberService.join(memberDto);

            ApiResponseDto<ResponseMemberDto> apiResponseDto = memberService.login(memberDto);

            assertThat(apiResponseDto.getHttpStatus().value()).isEqualTo(200);
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 CustomException이 발생한다")
        void loginFailNotMatchPassword() {
            assertThrows(CustomException.class, () -> {
                MemberDto memberDto = MemberDto.getMemberDto("테스트ID", passwordEncoder.encode("테스트PW"), "테스트", "010-1234-5678");
                memberService.join(memberDto);

                MemberDto loginDto = MemberDto.getLoginDto("테스트ID", "잘못된PW");
                memberService.login(loginDto);
            });
        }
    }
}
