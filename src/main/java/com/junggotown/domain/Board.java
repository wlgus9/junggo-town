package com.junggotown.domain;

import com.junggotown.dto.board.BoardDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String title;
    private String description;

    @Builder
    public Board(String userId, String title, String description) {
        this.userId = userId;
        this.title = title;
        this.description = description;
    }

    public static Board getBoardFromDto(BoardDto boardDto, String userId) {
        return Board.builder()
                .userId(userId)
                .title(boardDto.getTitle())
                .description(boardDto.getDescription())
                .build();
    }
}
