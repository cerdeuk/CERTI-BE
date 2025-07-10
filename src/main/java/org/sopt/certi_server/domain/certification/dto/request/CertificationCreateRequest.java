package org.sopt.certi_server.domain.certification.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CertificationCreateRequest(
        @NotEmpty(message = "자격증 이름은 필수입니다.") String certificationName,
        @NotEmpty(message = "인증기관에 대한 정보는 필수입니다.") String agencyName,
        @NotEmpty(message = "자격증 종류는 필수입니다.") String certificationType,
        @NotEmpty(message = "시험 종류는 필수입니다.") String testType,
        String averagePeriod,
        Long charge,
        String description,
        String testDateInformation,
        @NotEmpty(message = "가장 가까운 시험 날짜를 입력해주세요.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
        LocalDate nearestTestDate,
        List<String> tags,
        String applicationMethod,
        String applicationUrl
) {
}
