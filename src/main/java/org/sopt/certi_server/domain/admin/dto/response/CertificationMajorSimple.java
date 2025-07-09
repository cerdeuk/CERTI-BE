package org.sopt.certi_server.domain.admin.dto.response;

import org.sopt.certi_server.domain.certification.entity.CertificationMajor;

public record CertificationMajorSimple(Long certificationMajorId, Long majorId, String majorName, float weight) {

    public static CertificationMajorSimple from(CertificationMajor cm){
        return new CertificationMajorSimple(cm.getId(), cm.getMajor().getId(), cm.getMajor().getName(), cm.getWeight());
    }
}
