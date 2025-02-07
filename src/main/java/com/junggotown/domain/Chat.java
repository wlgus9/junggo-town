package com.junggotown.domain;

import com.junggotown.dto.chat.ChatDto;
import com.junggotown.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long productId;
    private String sendUserId;
    private String receiveUserId;
    private String message;

    @Builder
    public Chat(Long productId, String sendUserId, String receiveUserId, String message) {
        this.productId = productId;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.message = message;
    }

    public static Chat getChatFromDto(ChatDto chatDto) {
        return Chat.builder()
                .productId(chatDto.getProductId())
                .sendUserId(chatDto.getSendUserId())
                .receiveUserId(chatDto.getReceiveUserId())
                .message(chatDto.getMessage())
                .build();
    }
}
