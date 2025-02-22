package com.junggotown.member;

import com.junggotown.domain.Member;
import com.junggotown.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final Member member = Member.builder()
                                    .userId("testId")
                                    .userPw("qwer1234!")
                                    .userName("홍길동")
                                    .userTelno("010-1234-5678")
                                    .build();

    @Test
    @DisplayName("회원 아이디로 조회한다")
    void findByUserId() {
        memberRepository.save(member);

        Optional<Member> find = memberRepository.findByUserId(member.getUserId());

        assertThat(find).isPresent();
    }

    @Test
    @DisplayName("해당 유저가 있는지 체크한다")
    void existsByUserId() {
        memberRepository.save(member);

        boolean isExists = memberRepository.existsByUserId(member.getUserId());

        assertThat(isExists).isEqualTo(true);
    }
}
