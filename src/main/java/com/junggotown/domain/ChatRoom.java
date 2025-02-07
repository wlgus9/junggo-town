package com.junggotown.domain;

import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "chat_room_id", updatable = false, nullable = false)
    private String id;

    private Long productId;
    private String buyerId;
    private String sellerId;

    @Builder
    public ChatRoom(Long productId, String buyerId, String sellerId) {
        this.productId = productId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }

    public static ChatRoom from(Long productId, String buyerId, String sellerId) {
        return ChatRoom.builder()
                .productId(productId)
                .buyerId(buyerId)
                .sellerId(sellerId)
                .build();
    }
}
