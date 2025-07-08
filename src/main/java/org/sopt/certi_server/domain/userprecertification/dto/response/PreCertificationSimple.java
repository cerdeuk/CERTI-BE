package org.sopt.certi_server.domain.userprecertification.dto.response;

import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;

public record PreCertificationSimple(
        Long certificationId,
        String certificationName,
        String averagePeriod,
        String testDate,
        String agencyName,
        String iconImageUrl
) {

    public static PreCertificationSimple from(UserPreCertification upc){
        return new PreCertificationSimple(upc.getCertification().getId(), upc.getCertification().getName(), upc.getCertification().getAveragePeriod(),upc.getCertification().getTestDateInformation(), upc.getCertification().getAgency().getName(), upc.getIconType().getIconImageUrl());
    }
}
