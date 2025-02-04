package com.junggotown.dto.board;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBoardDto {
    private Long id;

    @Builder
    public ResponseBoardDto(Long id) {
        this.id = id;
    }

    public static ResponseBoardDto from(Long id) {
        return ResponseBoardDto.builder()
                .id(id)
                .build();
    }
}
