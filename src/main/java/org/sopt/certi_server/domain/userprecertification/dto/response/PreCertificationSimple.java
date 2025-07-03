package org.sopt.certi_server.domain.userprecertification.dto.response;

import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;

import java.time.LocalDateTime;

public record PreCertificationSimple(
        Long certificationId,
        String certificationName,
        String averagePeriod,
        String testDate,
        String agencyName
) {

    public static PreCertificationSimple from(UserPreCertification upc){
        return new PreCertificationSimple(upc.getCertification().getId(), upc.getCertification().getName(), upc.getCertification().getAveragePeriod(),upc.getCertification().getTestDate(), upc.getCertification().getAgency().getName());
    }
}
