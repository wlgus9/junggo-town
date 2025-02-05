package com.junggotown.domain;

import com.junggotown.dto.member.MemberDto;
import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String userPw;
    private String userName;
    private String userTelno;

    @Builder
    public Member(String userId, String userPw, String userName, String userTelno) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userTelno = userTelno;
    }

    public static Member getMemberFromDto(MemberDto memberDto, BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .userId(memberDto.getUserId())
                .userPw(passwordEncoder.encode(memberDto.getUserPw()))
                .userName(memberDto.getUserName())
                .userTelno(memberDto.getUserTelno())
                .build();
    }
}
