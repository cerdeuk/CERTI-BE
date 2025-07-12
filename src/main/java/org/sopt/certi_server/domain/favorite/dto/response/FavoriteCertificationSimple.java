package org.sopt.certi_server.domain.favorite.dto.response;

import org.sopt.certi_server.domain.certification.entity.Certification;

public record FavoriteCertificationSimple(Long certificationId, boolean isFavorite, String certificationName,
                                          String certificationType, String testType, String agencyName) {

    public static FavoriteCertificationSimple from(Certification certification) {
        return new FavoriteCertificationSimple(certification.getId(), true, certification.getName(), certification.getCertificationType().getKoreanName(), certification.getTestType().getType(), certification.getAgency().getName());
    }
}
