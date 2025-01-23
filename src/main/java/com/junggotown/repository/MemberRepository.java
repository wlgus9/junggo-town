package com.junggotown.repository;

import com.junggotown.domain.Member;
import java.util.List;

public interface MemberRepository {
    void save(Member member);

    List<Member> findMember();
}
