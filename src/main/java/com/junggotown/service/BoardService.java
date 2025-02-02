package com.junggotown.service;

import com.junggotown.domain.Board;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.BoardDto;
import com.junggotown.global.JwtProvider;
import com.junggotown.global.ResponseMessage;
import com.junggotown.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ApiResponseDto write(BoardDto boardDto, HttpServletRequest request) {
        boolean isSuccess;
        String message;

        String token = jwtProvider.resolveToken(request);
        String userId = jwtProvider.getUserId(token);

        Board board = Board.builder()
                .userId(userId)
                .title(boardDto.getTitle())
                .description(boardDto.getDescription())
                .build();

        try {
            boardRepository.save(board);
            isSuccess = true;
            message = ResponseMessage.BOARD_WRTIE_SUCCESS;
        } catch (Exception e) {
            isSuccess = false;
            message = ResponseMessage.BOARD_WRTIE_FAIL;
        }

        return ApiResponseDto.builder()
                .success(isSuccess)
                .message(message)
                .build();
    }
}
