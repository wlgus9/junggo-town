package com.junggotown.controller;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.BoardDto;
import com.junggotown.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(name = "중고거래 게시판", description = "중고거래 게시판 api")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "글 작성", description = "제목과 상세설명을 입력하여 글을 작성합니다."
            , parameters = {@Parameter(name = "title"), @Parameter(name = "description")}
    )
    @PostMapping("/write")
    public ResponseEntity<ApiResponseDto> write(
            @RequestBody
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "글 작성에 필요한 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BoardDto.class)
                    )) BoardDto boardDto, HttpServletRequest request
    ) {
        ApiResponseDto apiResponseDto = boardService.write(boardDto, request);

        return ResponseEntity.status(apiResponseDto.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseDto);
    }
}
