package org.sopt.certi_server.domain.user.dto.type;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NicknameValidationType {

    VALID(true, "사용 가능한 닉네임입니다."),
    EMPTY(false, "닉네임은 공백일 수 없습니다."),
    TOO_LONG(false, "닉네임은 7자 이하만 가능합니다."),
    DUPLICATE(false, "이미 존재하는 닉네임입니다."),
    PROFANITY(false, "닉네임에 비속어를 포함할 수 없습니다.");

    private final boolean isSuccess;

    private final String message;
}
