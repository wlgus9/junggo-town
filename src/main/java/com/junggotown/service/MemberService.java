package com.junggotown.service;

import com.junggotown.domain.Member;

import java.util.List;

public interface MemberService {

    void join(Member member);
    boolean login(Member member);
    List<Member> findMemberById(String userId);
    void updateMemberByUserIdAndUserPw(String userId, String userPw, String  originId);
    void deleteMemberByUserId(String userId);
}
