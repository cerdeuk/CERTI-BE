package org.sopt.certi_server.domain.certification.dto.request;

public record CertificationCreateRequest(
        String certificationName,
        Long agencyId,
        String testType,
        String averagePeriod,
        Long charge,
        String description,
        String testDate,
        String applicationMethod,
        String cardImageUrl,
        String applicationUrl

) {
}
