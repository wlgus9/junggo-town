package com.junggotown.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null인 필드 제외
public class ResponseMemberDto {
    private Long id;
    private String token;

    @Builder
    public ResponseMemberDto(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static ResponseMemberDto getJoinDto(Long id) {
        return ResponseMemberDto.builder()
                .id(id)
                .build();
    }

    public static ResponseMemberDto getLoginDto(String token) {
        return ResponseMemberDto.builder()
                .token(token)
                .build();
    }
}
