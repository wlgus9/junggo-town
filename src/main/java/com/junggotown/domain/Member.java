package com.junggotown.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Member {
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
}
