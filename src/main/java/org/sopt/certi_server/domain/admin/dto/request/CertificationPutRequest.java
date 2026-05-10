package org.sopt.certi_server.domain.admin.dto.request;

import java.util.List;

public record CertificationPutRequest(
        Long certificationId,
        String certificationName,
        List<String> tags,
        String averagePeriod,
        String charge,
        String agencyName,
        String testType,
        String description,
        String testDateInformation,
        String applicationMethod,
        String applicationUrl,
        String expirationPeriod
) {
}
