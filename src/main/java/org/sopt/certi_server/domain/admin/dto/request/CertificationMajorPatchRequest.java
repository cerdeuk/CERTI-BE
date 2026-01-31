package org.sopt.certi_server.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record CertificationMajorPatchRequest(
        @NotNull(message = "자격증 id는 필수값입니다.") Long certificationId,
        @NotNull(message = "전공 id는 필수값입니다.") Long majorImplId,
        @NotNull(message = "가중치 값은 필수입니다.") Double weight
) {
}
