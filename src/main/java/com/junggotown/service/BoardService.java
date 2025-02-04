package com.junggotown.service;

import com.junggotown.domain.Board;
import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.board.BoardDto;
import com.junggotown.dto.board.ResponseBoardDto;
import com.junggotown.global.exception.board.BoardException;
import com.junggotown.global.jwt.JwtProvider;
import com.junggotown.global.message.ResponseMessage;
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
    public ApiResponseDto<ResponseBoardDto> write(BoardDto boardDto, HttpServletRequest request) throws BoardException {
        Board board = Board.getBoardFromDto(boardDto, jwtProvider.getUserId(request));
        Long id = boardRepository.save(board).getId();

        return ApiResponseDto.response(ResponseMessage.BOARD_WRITE_SUCCESS, ResponseBoardDto.from(id));
    }
}
