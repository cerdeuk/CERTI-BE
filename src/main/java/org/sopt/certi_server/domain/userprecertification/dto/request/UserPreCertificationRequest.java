package org.sopt.certi_server.domain.userprecertification.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserPreCertificationRequest(@NotNull(message = "자격증 id에 대한 정보가 누락되었습니다.") Long certificationId) {
}
