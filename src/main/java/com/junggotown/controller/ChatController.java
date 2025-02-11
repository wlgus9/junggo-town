package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.chat.ChatDto;
import com.junggotown.dto.chat.ResponseChatDto;
import com.junggotown.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "채팅", description = "채팅 api")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅 전송", description = "상품 아이디, 메세지를 입력하여 채팅을 전송합니다.")
    @PostMapping("/send")
    public ApiResponseDto<ResponseChatDto> send(@RequestBody @Valid ChatDto chatDto, HttpServletRequest request) {
        return chatService.send(chatDto, request);
    }

    @Operation(summary = "채팅 조회", description = "상품 아이디를 입력하여 해당 상품에 대한 나의 채팅 내역을 조회합니다.")
    @GetMapping("/search")
    public ApiResponseDto<List<ResponseChatDto>> search(@RequestParam("productId") Long productId, HttpServletRequest request) {
        return chatService.search(productId, request);
    }

    @Operation(summary = "채팅 전체 조회", description = "나의 채팅 내역 전체를 조회합니다.")
    @GetMapping("/search-all")
    public ApiResponseDto<Map<String, List<ResponseChatDto>>> searchAll(HttpServletRequest request) {
        return chatService.searchAll(request);
    }
}
