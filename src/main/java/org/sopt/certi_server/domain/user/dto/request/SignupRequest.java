package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;
import org.sopt.certi_server.global.annotation.NoProfanity;

import java.util.List;

public record SignupRequest(
        @NotNull(message = "사용자 정보가 누락되었습니다") OAuthUserInformation userInformation,
        @NotEmpty(message = "대학교 정보는 필수입니다.") String university,
        @NotEmpty(message = "학년 정보는 필수입니다.") String grade,
        @NotEmpty(message = "계열 정보는 필수입니다.") String track,
        @NotEmpty(message = "전공 정보는 필수입니다.") String major,
        @NotEmpty(message = "닉네임 정보는 필수입니다.") @Size(min = 1, max = 7, message = "닉네임은 최소 1자, 최대 7자까지 입력 해야 합니다.") @NoProfanity String nickname,
        @NotEmpty(message = "직무 정보는 필수입니다.") @Size(min = 1, max = 3, message = "직무는 최소 1개, 최대 3개까지 선택 가능합니다.") List<String> jobs
) {
}
