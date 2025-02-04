package com.junggotown.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseMemberDto {
    private Long id;
    private String token;

    @Builder
    public ResponseMemberDto(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static ResponseMemberDto fromId(Long id) {
        return ResponseMemberDto.builder()
                .id(id)
                .build();
    }

    public static ResponseMemberDto fromToken(String token) {
        return ResponseMemberDto.builder()
                .token(token)
                .build();
    }
}
