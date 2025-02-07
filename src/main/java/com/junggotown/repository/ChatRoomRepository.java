package com.junggotown.repository;

import com.junggotown.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findFirstByProductId(Long productId);

    Optional<ChatRoom> findByProductIdAndBuyerId(Long productId, String buyerId);
}
