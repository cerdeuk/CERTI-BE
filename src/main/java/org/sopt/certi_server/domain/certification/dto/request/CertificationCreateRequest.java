package org.sopt.certi_server.domain.certification.dto.request;

import java.time.LocalDate;
import java.util.List;

public record CertificationCreateRequest(
        String certificationName,
        Long agencyId,
        String certificationType,
        String testType,
        String averagePeriod,
        Long charge,
        String description,
        String testDateInformation,
        String nearestTestDate,
        List<String> tags,
        String applicationMethod,
        String cardImageUrl,
        String applicationUrl
) {
}
