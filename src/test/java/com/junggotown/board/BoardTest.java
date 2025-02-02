package com.junggotown.board;

import com.junggotown.dto.ApiResponseDto;
import com.junggotown.dto.BoardDto;
import com.junggotown.service.BoardService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardTest {

    @Autowired
    BoardService boardService;

    @Test
    void 글_작성() {
        BoardDto boardDto = BoardDto.builder()
                        .title("test")
                        .description("testDesc")
                        .build();

        ApiResponseDto apiResponseDto = boardService.write(boardDto);

        assertThat(apiResponseDto.isSuccess()).isEqualTo(true);
    }
}
