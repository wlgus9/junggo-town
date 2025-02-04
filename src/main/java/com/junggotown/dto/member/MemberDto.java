package com.junggotown.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Schema(description = "사용자")
public class MemberDto {
    @NotBlank
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9]{3,19}$",
            message = "아이디는 영문자로 시작해야 하며, 영문자와 숫자로만 이루어져야 합니다."
    )
    @Schema(description = "아이디 (영문자로 시작, 영문+숫자 가능, 4~20자)", example = "user123")
    private String userId;

    @NotBlank
    @Size(min = 4, message = "비밀번호는 최소 4자 이상이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,}$",
            message = "비밀번호는 최소 4자 이상이며, 문자, 숫자, 특수문자를 포함해야 합니다."
    )
    @Schema(description = "비밀번호 (4자 이상, 문자+숫자+특수문자 포함)", example = "A1@b")
    private String userPw;

    @NotBlank
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Pattern(
            regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
            message = "전화번호는 010-1234-5678 형식이어야 합니다."
    )
    @Schema(description = "전화번호 (010-1234-5678 형식)", example = "010-1234-5678")
    private String userTelno;

    @Builder
    public MemberDto(String userId, String userPw, String userName, String userTelno) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userTelno = userTelno;
    }

    public static MemberDto getMemberDto(String userId, String userPw, String userName, String userTelno) {
        return MemberDto.builder()
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .userTelno(userTelno)
                .build();
    }

    public static MemberDto getLoginDto(String userId, String userPw) {
        return MemberDto.builder()
                .userId(userId)
                .userPw(userPw)
                .build();
    }
}
