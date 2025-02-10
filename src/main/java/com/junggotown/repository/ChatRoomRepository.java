package com.junggotown.repository;

import com.junggotown.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findFirstByProductId(Long productId);

    Optional<ChatRoom> findByProductIdAndBuyerId(Long productId, String buyerId);

    @Query("SELECT cr FROM ChatRoom cr WHERE :userId IN (cr.buyerId, cr.sellerId)")
    Optional<List<ChatRoom>> findByUserIdIn(String userId);
}
