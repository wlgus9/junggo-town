package com.junggotown.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Schema(description = "채팅")
public class ChatDto {

    @NotNull
    @Schema(description = "상품 아이디")
    private Long productId;

    @Schema(description = "발신자 아이디", hidden = true)
    private String sendUserId;

    @Schema(description = "수신자 아이디", hidden = true)
    private String receiveUserId;

    @NotBlank
    @Schema(description = "채팅 메세지")
    private String message;

    @Builder
    public ChatDto(Long productId, String sendUserId, String receiveUserId, String message) {
        this.productId = productId;
        this.sendUserId = sendUserId;
        this.receiveUserId = receiveUserId;
        this.message = message;
    }

    public static ChatDto getCreateDto(ChatDto chatDto, String sendUserId, String receiveUserId) {
        return ChatDto.builder()
                .productId(chatDto.getProductId())
                .sendUserId(sendUserId)
                .receiveUserId(receiveUserId)
                .message(chatDto.getMessage())
                .build();
    }

    public static ChatDto getCreateDto(Long productId, String sendUserId, String receiveUserId, String message) {
        return ChatDto.builder()
                .productId(productId)
                .sendUserId(sendUserId)
                .receiveUserId(receiveUserId)
                .message(message)
                .build();
    }
}
