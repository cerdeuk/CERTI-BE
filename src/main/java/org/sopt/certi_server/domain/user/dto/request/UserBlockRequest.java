package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserBlockRequest(
        @NotNull(message = "차단하려는 사용자의 ID는 필수값입니다.") Long userId
) {
}
