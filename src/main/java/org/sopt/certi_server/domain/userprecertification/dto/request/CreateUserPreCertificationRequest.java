package org.sopt.certi_server.domain.userprecertification.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateUserPreCertificationRequest(
        @NotNull(message = "자격증 id 정보는 필수입니다.") Long certificationId,
        @Nullable String city,
        @Nullable String state,
        @Nullable
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime testDate
) {
}
