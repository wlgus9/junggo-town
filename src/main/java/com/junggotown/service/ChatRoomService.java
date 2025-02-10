package com.junggotown.service;

import com.junggotown.domain.ChatRoom;
import com.junggotown.domain.Product;
import com.junggotown.global.common.ResponseMessage;
import com.junggotown.global.exception.CustomException;
import com.junggotown.repository.ChatRoomRepository;
import com.junggotown.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ChatRoom getOrCreateChatRoom(Long productId, String userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ResponseMessage.PRODUCT_IS_NOT_EXISTS));

        String sellerId = product.getUserId();
        String buyerId;

        // 현재 userId가 판매자인지 구매자인지 판단
        if (userId.equals(sellerId)) {
            // 판매자가 먼저 채팅을 시작하는 경우
            // 기존에 이 상품에 대해 구매자가 메시지를 보낸 기록이 있는지 확인
            buyerId = chatRoomRepository.findFirstByProductId(productId)
                    .map(ChatRoom::getBuyerId)
                    .orElseThrow(() -> new CustomException(ResponseMessage.PRODUCT_BUYER_IS_NOT_EXISTS));
        } else {
            // 구매자가 먼저 채팅을 시작하는 경우
            buyerId = userId;
        }

        return chatRoomRepository.findByProductIdAndBuyerId(productId, buyerId)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.from(productId, buyerId, sellerId)));
    }
}
