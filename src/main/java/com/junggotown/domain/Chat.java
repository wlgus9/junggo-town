package com.junggotown.domain;

import com.junggotown.dto.chat.ChatDto;
import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "chatRoomId", nullable = false)
    private ChatRoom chatRoom;

    private Long productId;
    private String sendUserId;
    private String receiveUserId;
    private String message;

    @Builder
    public Chat(ChatRoom chatRoom, Long productId, String sendUserId, String receiveUserId, String message) {
        this.chatRoom = chatRoom;
        this.productId = productId;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.message = message;
    }

    public static Chat getChatFromDto(ChatDto chatDto, ChatRoom chatRoom) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .productId(chatDto.getProductId())
                .sendUserId(chatDto.getSendUserId())
                .receiveUserId(chatDto.getReceiveUserId())
                .message(chatDto.getMessage())
                .build();
    }

    public String getChatRoomId() {
        return this.chatRoom != null ? this.chatRoom.getChatRoomId().toString() : null;
    }
}
