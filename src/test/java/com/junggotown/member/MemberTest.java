package com.junggotown.member;

import com.junggotown.domain.Member;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.MemberDto;
import com.junggotown.repository.MemberRepository;
import com.junggotown.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

@Transactional
public class MemberTest {

    MemberService memberService;

    // @BeforeEach
    // void beforeEach() {
    //     this.memberService = new MemberService(new MemberRepository());
    // }

    // @Test
    void 회원가입() {
        MemberDto memberDto = MemberDto.builder()
                            .userId("테스트ID")
                            .userPw("테스트PW")
                            .build();

        ApiResponseDto apiResponseDto = memberService.join(memberDto);

        assertThat(apiResponseDto.isSuccess()).isEqualTo(true);
    }
}
