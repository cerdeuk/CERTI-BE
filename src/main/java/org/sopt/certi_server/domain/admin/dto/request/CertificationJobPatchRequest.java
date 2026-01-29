package org.sopt.certi_server.domain.admin.dto.request;

import jakarta.validation.constraints.NotNull;

public record CertificationJobPatchRequest(
        @NotNull(message = "자격증 id는 필수값입니다.") Long certificationId,
        @NotNull(message = "직무 id는 필수값입니다.") Long jobId,
        @NotNull(message = "가중치 값은 필수입니다.") Double weight
) {
}
