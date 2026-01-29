package org.sopt.certi_server.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateAgreementRequest(
        @NotNull(message = "동의 상태는 필수값입니다.") Boolean isAgreed
) {
}
