package com.junggotown.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junggotown.domain.Chat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null인 필드 제외
public class ResponseChatDto {
    private Long id;
    private String chatRoomId;
    private Long productId;
    private String sendUserId;
    private String receiveUserId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ResponseChatDto(Long id, String chatRoomId, Long productId, String sendUserId, String receiveUserId, String message, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.productId = productId;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ResponseChatDto getSendDto(Chat chat) {
        return getSendDto(chat, null);
    }

    public static ResponseChatDto getSendDto(Chat chat, Long id) {
        return ResponseChatDto.builder()
                .id(id)
                .productId(chat.getProductId())
                .sendUserId(chat.getSendUserId())
                .receiveUserId(chat.getReceiveUserId())
                .message(chat.getMessage())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }

    public static ResponseChatDto getSendDtoChatRoomId(Chat chat, String chatRoomId) {
        return ResponseChatDto.builder()
                .id(chat.getId())
                .chatRoomId(chatRoomId)
                .productId(chat.getProductId())
                .sendUserId(chat.getSendUserId())
                .receiveUserId(chat.getReceiveUserId())
                .message(chat.getMessage())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }
}
