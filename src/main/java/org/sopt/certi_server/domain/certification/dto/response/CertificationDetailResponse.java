package org.sopt.certi_server.domain.certification.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.job.entity.Job;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CertificationDetailResponse(
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
    public static CertificationDetailResponse from(Certification certification) {
        return new CertificationDetailResponse(
                certification.getId(),
                certification.getName(),
                certification.getTags(),
                certification.getAveragePeriod(),
                certification.getCharge(),
                certification.getAgency().getName(),
                certification.getTestType().getType(),
                certification.getDescription(),
                certification.getTestDateInformation(),
                certification.getApplicationMethod(),
                certification.getApplicationUrl(),
                certification.getExpirationPeriod()
        );
    }
}
