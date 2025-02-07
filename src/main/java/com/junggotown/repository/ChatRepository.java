package com.junggotown.repository;

import com.junggotown.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByProductId(Long productId);

    @Query("SELECT c FROM Chat c WHERE c.productId = :productId AND (c.sendUserId = :userId OR c.receiveUserId = :userId) ORDER BY c.createdAt ASC")
    Optional<List<Chat>> findByProductIdAndUserId(@Param("productId")Long productId, @Param("userId") String userId);

    @Query("SELECT c FROM Chat c WHERE c.sendUserId = :userId OR c.receiveUserId = :userId ORDER BY c.createdAt ASC")
    Optional<List<Chat>> findByUserId(String userId);

    List<Chat> findByChatRoom_ChatRoomIdIn(List<String> chatRoomIds);
}
