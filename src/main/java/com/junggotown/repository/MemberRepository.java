package com.junggotown.repository;

import com.junggotown.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(String userId);
    boolean existsByUserId(String userId);
}
