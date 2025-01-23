package com.junggotown.service;

import com.junggotown.domain.Member;

import java.util.List;

public interface MemberService {

    void join(Member member);
    List<Member> findMember();
}
