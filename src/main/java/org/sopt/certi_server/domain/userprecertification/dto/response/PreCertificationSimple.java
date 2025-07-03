package org.sopt.certi_server.domain.userprecertification.dto.response;

import org.sopt.certi_server.domain.userprecertification.entity.UserPreCertification;

public record PreCertificationSimple(Long certificationId, String certificationName) {

    public static PreCertificationSimple from(UserPreCertification upc){
        return new PreCertificationSimple(upc.getCertification().getId(), upc.getCertification().getName());
    }
}
