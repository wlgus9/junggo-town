package com.junggotown.repository;

import com.junggotown.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {
    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public boolean existsByUserIdAndUserPw(Member member) {
        Long result = (Long) em.createNativeQuery("select exists (\n" +
                            "\tselect 1 from Member m where m.user_id = :userId and m.user_pw = :userPw\n" +
                        ")")
                .setParameter("userId", member.getUserId())
                .setParameter("userPw", member.getUserPw())
                .getSingleResult();

        return result == 1;
    }

    public List<Member> findMemberById(String userId) {
        Member member = em.find(Member.class, userId);
        return List.of(member);
    }

    public void updateMemberByUserIdAndUserPw(String userId, String userPw, String originId) {
        em.createNativeQuery("update Member set user_id = :userId, user_pw = :userPw where user_id = :originId")
                .setParameter("userId", userId)
                .setParameter("userPw", userPw)
                .setParameter("originId", originId)
                .executeUpdate();

    }

    public void deleteMemberByUserId(String userId) {
        em.createNativeQuery("delete from Member where user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
