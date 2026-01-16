package org.sopt.certi_server.domain.activity.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateActivityRequest(

        @Schema(description = "활동 시작일", example = "2024.05.24", type = "string")
        @NotNull(message = "활동 시작일은 필수 입력값입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate startAt,

        @Schema(description = "활동 종료일", example = "2024.05.28", type = "string")
        @NotNull(message = "활동 종료일은 필수 입력값입니다")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate endAt,
        @NotNull(message = "소속은 필수 입력값입니다")
        @Size(max = 10, message = "최대 10자까지 입력가능합니다.")
        String place,
        @NotNull(message = "활동은 필수 입력값입니다")
        @Size(max = 10, message = "최대 10자까지 입력가능합니다.")
        String name,
        @Size(max = 16, message = "최대 16자까지 입력가능합니다")
        @NotNull(message = "활동 관련 내용은 필수 입력값입니다")
        String description
) {
}
