package org.sopt.certi_server.domain.user.dto.request;

import org.sopt.certi_server.domain.user.entity.enums.SocialType;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "카카오 인증 코드가 필요합니다.") String code,
        @NotEmpty(message = "socialType을 반드시 지정해주어야 합니다.") String socialType
) {
}
