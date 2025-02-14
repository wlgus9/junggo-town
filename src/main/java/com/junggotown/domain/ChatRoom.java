package com.junggotown.domain;

import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID chatRoomId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

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
