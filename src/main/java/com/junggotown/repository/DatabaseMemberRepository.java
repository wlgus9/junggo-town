package com.junggotown.repository;

import com.junggotown.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatabaseMemberRepository implements MemberRepository  {

    public DatabaseMemberRepository(EntityManager em) {
        this.em = em;
    }

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public List<Member> findMember() {
        return em.createQuery("select m from member m", Member.class)
                .getResultList();
    }
}
