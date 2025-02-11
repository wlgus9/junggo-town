package com.junggotown.service;

import com.junggotown.domain.Chat;
import com.junggotown.domain.ChatRoom;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.chat.ChatDto;
import com.junggotown.dto.chat.ResponseChatDto;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.repository.ChatRepository;
import com.junggotown.repository.ChatRoomRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomService chatRoomService;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ApiResponseDto<ResponseChatDto> send(ChatDto chatDto, HttpServletRequest request) {
        String sendUserId = jwtProvider.getUserId(request);

        // 채팅방 가져오기 (없으면 생성)
        ChatRoom chatRoom = chatRoomService.getOrCreateChatRoom(chatDto.getProductId(), sendUserId);

        // 받는 사람 결정
        String receiveUserId = sendUserId.equals(chatRoom.getSellerId())
                ? chatRoom.getBuyerId() // 보낸 사람이 판매자면, 받는 사람은 구매자
                : chatRoom.getSellerId(); // 보낸 사람이 구매자면, 받는 사람은 판매자

        // 메시지 저장
        Chat chat = Chat.getChatFromDto(ChatDto.getCreateDto(chatDto, sendUserId, receiveUserId), chatRoom);

        Long id = chatRepository.save(chat).getId();

        return ApiResponseDto.response(ResponseMessage.CHAT_SEND_SUCCESS, ResponseChatDto.getSendDto(chat, id));
    }

    public ApiResponseDto<List<ResponseChatDto>> search(Long productId, HttpServletRequest request) {
        return chatRepository.findByProductIdAndUserId(productId, jwtProvider.getUserId(request))
                .filter(chats -> !chats.isEmpty())
                .map(chats -> {
                    List<ResponseChatDto> returnDto = chats.stream()
                            .map(ResponseChatDto::getSendDto)
                            .collect(Collectors.toList());
                    return ApiResponseDto.response(ResponseMessage.CHAT_SEARCH_SUCCESS, returnDto);
                })
                .orElseGet(() -> ApiResponseDto.response(ResponseMessage.CHAT_IS_EMPTY));
    }

    public ApiResponseDto<Map<String, List<ResponseChatDto>>> searchAll(HttpServletRequest request) {
        String userId = jwtProvider.getUserId(request);

        // 사용자가 포함된 모든 채팅방 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdIn(userId)
                .filter(chatRoomList -> !chatRoomList.isEmpty())
                .orElseThrow(() -> new CustomException(ResponseMessage.CHAT_IS_EMPTY));

        // 채팅방 ID 리스트 추출
        List<String> chatRoomIds = chatRooms.stream()
                .map(ChatRoom::getChatRoomId)
                .collect(Collectors.toList());

        // 해당 채팅방들에 속한 채팅 메시지 조회
        List<Chat> chatList = chatRepository.findByChatRoom_ChatRoomIdIn(chatRoomIds)
                .filter(chats -> !chats.isEmpty())
                .orElseThrow(() -> new CustomException(ResponseMessage.CHAT_IS_EMPTY));

        // 채팅방 ID 기준으로 그룹화
        Map<String, List<ResponseChatDto>> groupedChats = chatList.stream()
                .collect(Collectors.groupingBy(
                        Chat::getChatRoomId,
                        Collectors.mapping(chat -> ResponseChatDto.getSendDtoChatRoomId(chat, chat.getChatRoomId()), Collectors.toList())
                ));

        return ApiResponseDto.response(ResponseMessage.CHAT_SEARCH_SUCCESS, groupedChats);
    }
}
