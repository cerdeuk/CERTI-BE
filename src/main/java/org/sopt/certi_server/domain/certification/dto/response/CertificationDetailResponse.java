package org.sopt.certi_server.domain.certification.dto.response;

import org.sopt.certi_server.domain.certification.entity.Category;
import org.sopt.certi_server.domain.certification.entity.Certification;

import java.util.List;
import java.util.stream.Collectors;

public record CertificationDetailResponse(
        Long certificationId,
        String certificationName,
        List<String> categories,
        String averagePeriod,
        Long charge,
        String agency,
        String applicationUrl,
        String description,
        String applicationMethod

) {
    public static CertificationDetailResponse from(Certification certification, List<Category> categories){
        return new CertificationDetailResponse(
                certification.getId(),
                certification.getName(),
                categories.stream().map(Category::getName).collect(Collectors.toList()),
                certification.getAveragePeriod(),
                certification.getCharge(),
                certification.getAgency().getName(),
                certification.getApplicationUrl(),
                certification.getDescription(),
                certification.getApplicationMethod()
        );
    }
}
