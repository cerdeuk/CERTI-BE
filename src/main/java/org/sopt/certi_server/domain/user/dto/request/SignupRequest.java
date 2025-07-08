package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.sopt.certi_server.domain.user.dto.response.OAuthUserInformation;

import java.util.List;

public record SignupRequest(
        @NotNull(message = "사용자 정보가 누락되었습니다") OAuthUserInformation userInformation,
        @NotEmpty(message = "대학교 정보는 필수입니다.") String university,
        @NotEmpty(message = "학년 정보는 필수입니다.") String grade,
        @NotEmpty(message = "계열 정보는 필수입니다.") String track,
        @NotEmpty(message = "전공 정보는 필수입니다.") String major,
        @NotEmpty(message = "직무 정보는 필수입니다.") List<String> job
) {
}
