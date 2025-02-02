package com.junggotown.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@Schema(description = "중고거래 글")
public class BoardDto {
    @NotBlank
    @Size(min = 4, max = 100, message = "글 제목은 4~100자여야 합니다.")
    @Schema(description = "글 제목 (최대 100자)")
    private String title;

    @NotBlank
    @Size(min = 4, max = 500, message = "글 상세설명은 4~500자여야 합니다.")
    @Schema(description = "글 상세설명 (최대 500자)")
    private String description;

    @Builder
    public BoardDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
