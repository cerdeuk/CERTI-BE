package org.sopt.certi_server.domain.admin.dto.response;

import lombok.Builder;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AdminCertificationResponse(
        Long certificationId,
        String certificationName,
        String agencyName,
        String certificationType,
        String testType,
        String averagePeriod,
        String charge,

        List<String> tags,
        String description,
        String testDateInformation,

        LocalDate nearestTestDate,
        String applicationMethod,
        String applicationUrl
) {


    public static AdminCertificationResponse from(Certification certification) {
        return AdminCertificationResponse.builder()
                .certificationId(certification.getId())
                .certificationName(certification.getName())
                .agencyName(certification.getAgency() == null ? "없음" : certification.getAgency().getName())
                .certificationType(certification.getCertificationType().getKoreanName())
                .testType(certification.getTestType().getType())
                .averagePeriod(certification.getAveragePeriod())
                .charge(certification.getCharge())
                .tags(certification.getTags())
                .description(certification.getDescription())
                .testDateInformation(certification.getTestDateInformation())
                .nearestTestDate(certification.getNearestTestDate())
                .applicationMethod(certification.getApplicationMethod())
                .applicationUrl(certification.getApplicationUrl())
                .build();
    }
}
