package org.sopt.certi_server.domain.acquisition.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAcquisitionRequest(
        @NotNull(message = "자격증 id는 필수 값입니다.") Long certificationId
) {
}
