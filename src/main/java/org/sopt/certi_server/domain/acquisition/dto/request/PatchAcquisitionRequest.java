package org.sopt.certi_server.domain.acquisition.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatchAcquisitionRequest(

        @NotNull(message = "취득 날짜 정보를 입력해주세요")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate acquisitionDate,

        String grade
) {
}
