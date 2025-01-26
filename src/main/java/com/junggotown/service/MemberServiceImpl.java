package com.junggotown.service;

import com.junggotown.domain.Member;
import com.junggotown.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public boolean login(Member member) {
        return memberRepository.existsByUserIdAndUserPw(member);
    }

    @Override
    public List<Member> findMemberById(String userId) {
        return memberRepository.findMemberById(userId);
    }

    @Override
    public void updateMemberByUserIdAndUserPw(String userId, String userPw, String originId) {
        memberRepository.updateMemberByUserIdAndUserPw(userId, userPw, originId);
    }

    @Override
    public void deleteMemberByUserId(String userId) {
        memberRepository.deleteMemberByUserId(userId);
    }


}
