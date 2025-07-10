package org.sopt.certi_server.domain.certification.dto.response;

import java.util.List;

public record CertificationListResponse(List<CertificationSimple> certificationSimpleList) {

    public static CertificationListResponse of(List<CertificationSimple> certificationSimpleList){
        return new CertificationListResponse(certificationSimpleList);
    }
}
