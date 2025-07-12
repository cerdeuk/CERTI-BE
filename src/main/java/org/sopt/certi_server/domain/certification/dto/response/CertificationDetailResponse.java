package org.sopt.certi_server.domain.certification.dto.response;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.job.entity.Job;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;

@Builder
public record CertificationDetailResponse(
        Long certificationId,
        String certificationName,
        List<String> tags,
        String averagePeriod,
        Long charge,
        String agencyName,
        String testType,
        String description,
        String testDateInformation,
        String applicationMethod,
        String applicationUrl,
        String expirationPeriod


) {
    public static CertificationDetailResponse from(Certification certification, List<Job> jobs){
        return new CertificationDetailResponse(
                certification.getId(),
                certification.getName(),
                jobs.stream().map(Job::getName).collect(Collectors.toList()),
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
