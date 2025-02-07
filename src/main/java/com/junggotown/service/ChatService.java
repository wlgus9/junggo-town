package com.junggotown.service;

import com.junggotown.domain.Chat;
import com.junggotown.domain.ChatRoom;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.chat.ChatDto;
import com.junggotown.dto.chat.ResponseChatDto;
import com.junggotown.global.exception.chat.ChatException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.message.ResponseMessage;
import com.junggotown.repository.ChatRepository;
import com.junggotown.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final ChatRepository chatRepository;
    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;

    public ApiResponseDto<ResponseChatDto> send(ChatDto chatDto, HttpServletRequest request) throws ChatException {
        String userId = jwtProvider.getUserId(request);

        // 채팅방 가져오기 (없으면 생성)
        ChatRoom chatRoom = chatRoomService.getOrCreateChatRoom(chatDto.getProductId(), userId);

        // 보내는 사람과 받는 사람 결정
        String sendUserId = userId;
        String receiveUserId = sendUserId.equals(chatRoom.getSellerId())
                ? chatRoom.getBuyerId() // 보낸 사람이 판매자면, 받는 사람은 구매자
                : chatRoom.getSellerId(); // 보낸 사람이 구매자면, 받는 사람은 판매자

        // 메시지 저장
        Chat chat = Chat.getChatFromDto(ChatDto.getCreateDto(chatDto, sendUserId, receiveUserId));

        Long id = chatRepository.save(chat).getId();

        return ApiResponseDto.response(ResponseMessage.CHAT_SEND_SUCCESS, ResponseChatDto.getSendDto(chat, id));
    }

    public ApiResponseDto<List<ResponseChatDto>> searchByProductIdAndUserId(Long productId, HttpServletRequest request) throws ChatException {
        Optional<List<Chat>> chatList = chatRepository.findByProductIdAndUserId(productId, jwtProvider.getUserId(request));

        return chatList
                .filter(chats -> !chats.isEmpty())
                .map(chats -> {
                    List<ResponseChatDto> returnDto = chats.stream()
                            .map(ResponseChatDto::getSendDto)
                            .collect(Collectors.toList());
                    return ApiResponseDto.response(ResponseMessage.CHAT_SEARCH_SUCCESS, returnDto);
                })
                .orElseGet(() -> ApiResponseDto.response(ResponseMessage.CHAT_IS_EMPTY));
    }

    public ApiResponseDto<List<ResponseChatDto>> searchAll(HttpServletRequest request) throws ChatException {
        Optional<List<Chat>> chatList = chatRepository.findByUserId(jwtProvider.getUserId(request));

        return chatList
                .filter(chats -> !chats.isEmpty())
                .map(chats -> {
                    List<ResponseChatDto> returnDto = chats.stream()
                            .map(ResponseChatDto::getSendDto)
                            .collect(Collectors.toList());
                    return ApiResponseDto.response(ResponseMessage.CHAT_SEARCH_SUCCESS, returnDto);
                })
                .orElseGet(() -> ApiResponseDto.response(ResponseMessage.CHAT_IS_EMPTY));
    }
}
