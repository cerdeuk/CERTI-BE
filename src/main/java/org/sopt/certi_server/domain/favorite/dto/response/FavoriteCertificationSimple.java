package org.sopt.certi_server.domain.favorite.dto.response;

import org.sopt.certi_server.domain.certification.entity.Certification;
import org.sopt.certi_server.domain.certification.entity.enums.TestType;

public record FavoriteCertificationSimple(Long certificationId, String certificationName, String testType, String agencyName) {

    public static FavoriteCertificationSimple from(Certification certification){
        return new FavoriteCertificationSimple(certification.getId(), certification.getName(), certification.getTestType().getType(), certification.getAgency().getName());
    }
}
