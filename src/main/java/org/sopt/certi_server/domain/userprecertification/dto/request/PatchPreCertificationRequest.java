package org.sopt.certi_server.domain.userprecertification.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PatchPreCertificationRequest(
        @NotNull(message = "날짜 정보를 입력해주세요")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime testDate,

        @NotBlank(message = "시험 장소(시/도)를 입력해주세요") String city,
        @NotBlank(message = "시험 장소(시/군/구)를 입력해주세요") String state
) {
}
